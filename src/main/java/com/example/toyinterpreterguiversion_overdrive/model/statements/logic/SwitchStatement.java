package com.example.toyinterpreterguiversion_overdrive.model.statements.logic;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.IExpression;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.stack.StackADT;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class SwitchStatement implements IStatement{
    IExpression expr;
    Map<IExpression, IStatement> cases;  //NOT USING KEY-ORDERED DICTIONARY ADT, NEED INSERTION-ORDER
    IStatement defaultCase;

    public SwitchStatement(IExpression ex, Map<IExpression, IStatement> cs, IStatement defC) {
        expr = ex; cases = cs; defaultCase = defC;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StandardException, IOException {
        Map<IValue, IStatement> valuesStatementRelation = new LinkedHashMap<>();
        StackADT<IStatement> exeStack = state.getExeStack();

        for (Map.Entry<IExpression, IStatement> entry : cases.entrySet()) {
            valuesStatementRelation.put(entry.getKey().evaluate(state.getSymTable(), state.getHeap()), entry.getValue());
        }

        IValue result = expr.evaluate(state.getSymTable(), state.getHeap());
        for (Map.Entry<IValue, IStatement> entry : valuesStatementRelation.entrySet()) {
            if (entry.getKey().equals(result)) {
                IStatement statementToExecute = entry.getValue();
                exeStack.push(statementToExecute);
                return null;
            }
        }

        exeStack.push(defaultCase);
        return null;
    }

    @Override
    public DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        IType typeExpr = expr.typecheck(typeEnv);
        for (IExpression switchExpr : cases.keySet()) {
            if (!switchExpr.typecheck(typeEnv).equals(typeExpr)) {
                throw new StandardException("Type mismatch inside switch cases");
            }
        }

        for (IStatement switchStmt : cases.values()) {
            switchStmt.typecheck(typeEnv);
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        StringBuilder body = new StringBuilder();
        String result = String.format("switch(%s) {", expr.toString());
        for (Map.Entry<IExpression, IStatement> entry : cases.entrySet()) {
            body.append(String.format("(case %s : %s), ", entry.getKey().toString(), entry.getValue().toString()));
        }
        body.setLength(body.length() - 2);
        result += body.toString();
        result += "}";
        return result;
    }

}
