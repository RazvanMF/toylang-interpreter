package com.example.toyinterpreterguiversion_overdrive.extras;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.*;
import com.example.toyinterpreterguiversion_overdrive.model.statements.*;
import com.example.toyinterpreterguiversion_overdrive.model.statements.assignment.AssignStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.io.PrintStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.io.VariableDeclarationStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.logic.IfStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.BoolType;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.types.IntType;
import com.example.toyinterpreterguiversion_overdrive.model.types.StringType;
import com.example.toyinterpreterguiversion_overdrive.model.values.BoolValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.IntValue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PrototypeParser {
    List<String> declarators = Arrays.asList("int", "string", "bool");
    List<String> assigners = List.of("=");
    List<String> executors = List.of("print");
    List<String> operators = Arrays.asList("+", "-", "*", "/");
    List<String> comparators = Arrays.asList("==", "<=", ">=", "!=", "<", ">");
    List<String> gates = Arrays.asList("AND", "OR");
    List<String> conditionals = List.of("IF");
    ArrayList<IStatement> exeQueue = new ArrayList<>();

    public IStatement fileToStatement(String fileName) throws StandardException, IOException {
        ArrayList<String> emuStack = new ArrayList<>();
        BufferedReader fin = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = fin.readLine()) != null) {
            emuStack.add(line);
        }

        for (String expression : emuStack) {
            //declarators
            boolean figuredType = false;

            for (String declarator : declarators) {
                if (expression.contains(declarator) && !expression.contains("print")) {
                    String[] tokens = expression.split(" ");
                    VariableDeclarationStatement resultParse = stringToDeclaration(tokens[0].strip(), tokens[1].strip());
                    exeQueue.add(resultParse);
                    figuredType = true;
                    break;
                }
            }
            if (figuredType)
                continue;

            for (String assigner : assigners) {
                if (expression.contains(assigner) && !expression.contains("IF")) {
                    String[] tokens = expression.split("=", 2);
                    String lhs = tokens[0].strip(); String rhs = tokens[1].strip();
                    AssignStatement resultParse = stringToAssignment(lhs, rhs);
                    exeQueue.add(resultParse);
                    figuredType = true;
                    break;

                }
            }
            if (figuredType)
                continue;

            for (String executor : executors) {
                if (expression.contains(executor)) {
                    String name = expression.substring(expression.indexOf('(') + 1, expression.indexOf(')'));
                    PrintStatement resultParse = new PrintStatement(new VariableLookupExpression(name));
                    exeQueue.add(resultParse);
                    figuredType = true;
                    break;
                }
            }
            if (figuredType)
                continue;

            for (String conditional : conditionals) {
                if (expression.contains(conditional)) {
                    String[] tokens = expression.split(" ");
                    String fstParam = expression.substring(expression.indexOf("IF") + 2,
                            expression.indexOf("THEN")).strip();
                    String sndParam = expression.substring(expression.indexOf("THEN") + 4,
                            (expression.contains("ELSE") ? expression.indexOf("ELSE") : expression.length())).strip();
                    String trdParam = "";
                    //if (expression.contains("ELSE")) {
                    trdParam = expression.substring(expression.indexOf("ELSE") + 4, expression.length()).strip();
                    //}

                    IExpression condition = logicExpressionParser(fstParam);
                    AssignStatement ifstmt = stringToAssignment(sndParam.split("=")[0].strip(),
                            sndParam.split("=")[1].strip());
                    AssignStatement elsestmt = stringToAssignment(trdParam.split("=")[0].strip(),
                            trdParam.split("=")[1].strip());

                    IfStatement resultParse = new IfStatement(condition, ifstmt, elsestmt);
                    exeQueue.add(resultParse);
                    figuredType = true;
                    break;
                }
            }
            if (figuredType)
                continue;
        }

        //compounding
        Stack<IStatement> simulStack = new Stack<>();
        //reverse(exeQueue);
        for (IStatement stmt : exeQueue) {
            simulStack.push(stmt);
        }

        while (simulStack.size() != 1) {
            IStatement rhs = simulStack.pop();
            IStatement lhs = simulStack.pop();
            IStatement res = new CompoundStatement(lhs, rhs);
            simulStack.push(res);
        }

        return simulStack.peek();
    }

    VariableDeclarationStatement stringToDeclaration(String type, String name) {
        var newType = switch(type) {
            case "int":
                yield new IntType();
            case "string":
                yield new StringType();
            case "bool":
                yield new BoolType();
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        };
        return new VariableDeclarationStatement(name, ((IType)newType));
    }
    AssignStatement stringToAssignment(String name, String expr) throws StandardException {
        boolean foundOperator = false;
        for (String operator : operators) {
            if (expr.contains(operator)) {
                foundOperator = true;
                break;
            }
        }
        boolean foundComparator = false;
        for (String comparator : comparators) {
            if (expr.contains(comparator)) {
                foundComparator = true;
                break;
            }
        }
        boolean foundGate = false;
        for (String gate : gates) {
            if (expr.contains(gate)) {
                foundGate = true;
                break;
            }
        }

        if (expr.equals("true") || expr.equals("false")) {
            IExpression rhs = logicExpressionParser(expr);
            return new AssignStatement(name, rhs);
        }

        if (foundOperator || (!foundOperator && !foundComparator && !foundGate)) {
            IExpression rhs = arithExpressionParser(expr);
            return new AssignStatement(name, rhs);
        }

        if (foundGate || foundComparator) {
            IExpression rhs = logicExpressionParser(expr);
            return new AssignStatement(name, rhs);
        }


        return null;
    }
    IExpression arithExpressionParser(String expression) {
        Map<Character, Integer> operatorPrecedence = new HashMap<>();
        operatorPrecedence.put('+', 1);
        operatorPrecedence.put('-', 1);
        operatorPrecedence.put('*', 2);
        operatorPrecedence.put('/', 2);
        operatorPrecedence.put('(', 3);
        operatorPrecedence.put(')', 3);

        List<String> tokens = Arrays.asList(expression.split(""));

        //Stack<Integer> values = new Stack<>();
        Stack<IExpression> values = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int index = 0; index < tokens.size(); index++) {
            String token = tokens.get(index);
            if (token.charAt(0) == ' ')
                continue;

            if (token.charAt(0) >= '0' && token.charAt(0) <= '9') {
                StringBuilder num = new StringBuilder();
                while (token.charAt(0) >= '0' && token.charAt(0) <= '9') {
                    int digit = Integer.parseInt(token);
                    num.append(digit);
                    index += 1;
                    if (index < tokens.size())
                        token = tokens.get(index);
                    else
                        break;
                }
                int number = Integer.parseInt(num.toString());
                values.push(new ValueExpression(new IntValue(number)));
                if (index < tokens.size() && tokens.get(index).charAt(0) != ' ')
                    index--;
            }

            else if (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/")) {
                //check for operator precedence
                while (!operators.isEmpty() &&
                        (operatorPrecedence.get(token.charAt(0)) <= operatorPrecedence.get(operators.peek())) &&
                        operatorPrecedence.get(operators.peek()) != 3) {
                    char operator = operators.pop();
                    IExpression rhs = values.pop();
                    IExpression lhs = values.pop();
                    ArithmeticExpression result = new ArithmeticExpression(operator, lhs, rhs);
                    values.push(result);
                }
                operators.push(token.charAt(0));
            }

            else if (token.equals("("))
                operators.push(token.charAt(0));

            else if (token.equals(")")) {
                while(operators.peek() != '(') {
                    char operator = operators.pop();
                    IExpression rhs = values.pop();
                    IExpression lhs = values.pop();
                    ArithmeticExpression result = new ArithmeticExpression(operator, lhs, rhs);
                    values.push(result);
                }
                operators.pop();

            }
            else if (token.charAt(0) >= 'A' && token.charAt(0) <= 'z') {
                StringBuilder word = new StringBuilder();
                while (token.charAt(0) >= 'A' && token.charAt(0) <= 'z') {
                    String letter = token;
                    word.append(letter);
                    index += 1;
                    if (index < tokens.size())
                        token = tokens.get(index);
                    else
                        break;
                }
                String finalWord = word.toString();
                values.push(new VariableLookupExpression(finalWord));
                if (index < tokens.size() && tokens.get(index).charAt(0) != ' ')
                    index--;
            }
        }

        while (!operators.isEmpty()) {
            char operator = operators.pop();
            IExpression rhs = values.pop();
            IExpression lhs = values.pop();
            ArithmeticExpression result = new ArithmeticExpression(operator, lhs, rhs);
            values.push(result);
        }

        return values.peek();
    }
    IExpression logicExpressionParser(String expression) throws StandardException {
        Map<Character, Integer> operatorPrecedence = new HashMap<>();
        operatorPrecedence.put('|', 1);
        operatorPrecedence.put('&', 2);
        operatorPrecedence.put('!', 3);

        expression = expression.replace("AND", "&");
        expression = expression.replace("OR", "|");
        expression = expression.replace("NOT", "!");

        operatorPrecedence.put('>', 4);
        operatorPrecedence.put('<', 4);
        operatorPrecedence.put('=', 4); //equal
        operatorPrecedence.put('~', 4); //not equal
        operatorPrecedence.put('{', 4); //lower or equal
        operatorPrecedence.put('}', 4); //greater or equal

        expression = expression.replace("==", "=");
        expression = expression.replace("!=", "~");
        expression = expression.replace(">=", "}");
        expression = expression.replace("<=", "{");
        expression = expression.replace(">", ">");
        expression = expression.replace("<", "<");

        operatorPrecedence.put('(', 5);
        operatorPrecedence.put(')', 5);

        List<String> tokens = Arrays.asList(expression.split(""));

        Stack<IExpression> values = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int index = 0; index < tokens.size(); index++) {
            String token = tokens.get(index);
            if (token.charAt(0) == ' ')
                continue;

            if (token.charAt(0) >= 'A' && token.charAt(0) <= 'z') {
                StringBuilder word = new StringBuilder();
                while (token.charAt(0) >= 'A' && token.charAt(0) <= 'z') {
                    String letter = token;
                    word.append(letter);
                    index += 1;
                    if (index < tokens.size())
                        token = tokens.get(index);
                    else
                        break;
                }
                String finalWord = word.toString();
                if (finalWord.equals("true") || finalWord.equals("false")) {
                    boolean value = finalWord.equals("true") ? true : false;
                    values.push(new ValueExpression(new BoolValue(value)));
                }

                else
                    values.push(new VariableLookupExpression(finalWord));
                if (index < tokens.size() && tokens.get(index).charAt(0) != ' ')
                    index--;
            }

            else if (token.equals("("))
                operators.push(token.charAt(0));

            else if (token.equals(")")) {
                while(operators.peek() != '(') {
                    char operator = operators.pop();
                    IExpression rhs = values.pop();
                    IExpression lhs = values.pop();
                    String operatorLarge = switch(operator) {
                        case '!' -> "NOT";
                        case '|' -> "OR";
                        case '&' -> "AND";
                        case '=' -> "==";
                        case '~' -> "!=";
                        case '{' -> "<=";
                        case '}' -> ">=";
                        case '<' -> "<";
                        case '>' -> ">";
                        default -> throw new StandardException("baka");
                    };
                    if (operatorLarge.equals("NOT") || operatorLarge.equals("OR") || operatorLarge.equals("AND")) {
                        LogicExpression result = new LogicExpression(operatorLarge, lhs, rhs);
                        values.push(result);
                    }
                    else {
                        RelationalExpression result = new RelationalExpression(operatorLarge, lhs, rhs);
                        values.push(result);
                    }
                }
                operators.pop();
            }

            else {
                //check for operator precedence
                while (!operators.isEmpty() &&
                        (operatorPrecedence.get(token.charAt(0)) <= operatorPrecedence.get(operators.peek())) &&
                        operatorPrecedence.get(operators.peek()) != 5) {
                    char operator = operators.pop();
                    IExpression rhs = values.pop();
                    IExpression lhs = values.pop();
                    String operatorLarge = switch(operator) {
                        case '!' -> "NOT";
                        case '|' -> "OR";
                        case '&' -> "AND";
                        case '=' -> "==";
                        case '~' -> "!=";
                        case '{' -> "<=";
                        case '}' -> ">=";
                        case '<' -> "<";
                        case '>' -> ">";
                        default -> throw new StandardException("error");
                    };
                    if (operatorLarge.equals("NOT") || operatorLarge.equals("OR") || operatorLarge.equals("AND")) {
                        LogicExpression result = new LogicExpression(operatorLarge, lhs, rhs);
                        values.push(result);
                    }
                    else {
                        RelationalExpression result = new RelationalExpression(operatorLarge, lhs, rhs);
                        values.push(result);
                    }

                }
                operators.push(token.charAt(0));
            }


        }


        while (!operators.isEmpty()) {
            char operator = operators.pop();
            IExpression rhs = values.pop();
            IExpression lhs = values.pop();
            String operatorLarge = switch(operator) {
                case '!' -> "NOT";
                case '|' -> "OR";
                case '&' -> "AND";
                case '=' -> "==";
                case '~' -> "!=";
                case '{' -> "<=";
                case '}' -> ">=";
                case '<' -> "<";
                case '>' -> ">";
                default -> throw new StandardException("error");
            };
            if (operatorLarge.equals("NOT") || operatorLarge.equals("OR") || operatorLarge.equals("AND")) {
                LogicExpression result = new LogicExpression(operatorLarge, lhs, rhs);
                values.push(result);
            }
            else {
                RelationalExpression result = new RelationalExpression(operatorLarge, lhs, rhs);
                values.push(result);
            }
        }

        return values.peek();
    }

}
