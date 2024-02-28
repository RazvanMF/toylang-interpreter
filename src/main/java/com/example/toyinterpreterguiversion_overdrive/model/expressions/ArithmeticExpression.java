package com.example.toyinterpreterguiversion_overdrive.model.expressions;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.types.IntType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.IntValue;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.heap.HeapADT;

public class ArithmeticExpression implements IExpression {
    IExpression exp1, exp2;
    int operator;

    public ArithmeticExpression(char op, IExpression e1, IExpression e2) {
        exp1 = e1; exp2 = e2;
        operator = switch (op) {
            case '+' -> 1;
            case '-' -> 2;
            case '*' -> 3;
            case '/' -> 4;
            default -> 0;
        };
    }

    @Override
    public String toString() {
        String opString = switch (operator) {
            case 1 -> "+";
            case 2 -> "-";
            case 3 -> "*";
            case 4 -> "/";
            default -> "";
        };
        return String.format("(%s %s %s)", exp1.toString(), opString, exp2.toString());
    }

    @Override
    public IValue evaluate(DictionaryADT<String, IValue> table, HeapADT<IValue> heap) throws StandardException {
        IValue num1 = exp1.evaluate(table, heap);
        if (num1.getType().equals(new IntType())) {
            IValue num2 = exp2.evaluate(table, heap);
            if (num1.getType().equals(new IntType())) {
                IntValue vl1 = (IntValue)num1;
                IntValue vl2 = (IntValue)num2;
                int number1, number2;
                number1 = vl1.getValue(); number2 = vl2.getValue();
                int result = switch (operator) {
                    case 1 -> number1 + number2;
                    case 2 -> number1 - number2;
                    case 3 -> number1 * number2;
                    case 4 -> {
                        if (number2 != 0)
                            yield number1 / number2;
                        else
                            throw new StandardException("Division by zero.");
                    }
                    default -> throw new StandardException("Unexpected operator.");
                };
                return new IntValue(result);
            }
            else
                throw new StandardException("Second expression not an integer.");
        }
        else
            throw new StandardException("Second expression not an integer.");
    }

    @Override
    public IType typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        IType typ1, typ2;
        typ1 = exp1.typecheck(typeEnv);
        typ2 = exp2.typecheck(typeEnv);
        if (typ1.equals(new IntType())) {
            if (typ2.equals(new IntType())) {
                return new IntType();
            }
            else
                throw new StandardException("second operand is not an integer");
        }
        else
            throw new StandardException("first operand is not an integer");
    }


}
