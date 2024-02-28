package com.example.toyinterpreterguiversion_overdrive.model.statements.io;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.IExpression;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.list.ListADT;

public class PrintStatement implements IStatement {
    IExpression expr;
    public PrintStatement(IExpression e) {
        expr = e;
    }

    @Override
    public String toString() {
        return String.format("print(%s)", expr.toString());
    }

    @Override
    public ProgramState execute(ProgramState state) throws StandardException {
        ListADT<IValue> output = state.getOutput();
        output.add(expr.evaluate(state.getSymTable(), state.getHeap()));
        //return state;
        return null;
    }

    @Override
    public DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        expr.typecheck(typeEnv);
        return typeEnv;
    }

}
