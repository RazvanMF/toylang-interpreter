package com.example.toyinterpreterguiversion_overdrive.model.expressions;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.types.BoolType;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.values.BoolValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.heap.HeapADT;

public class LogicNotExpression implements IExpression {
    IExpression exprToEvaluate;

    public LogicNotExpression(IExpression expr) {
        exprToEvaluate = expr;
    }

    @Override
    public IValue evaluate(DictionaryADT<String, IValue> table, HeapADT<IValue> heap) throws StandardException {
        IValue result = exprToEvaluate.evaluate(table, heap);
        if (result instanceof BoolValue boolres) {
            return new BoolValue(!boolres.getValue());
        }
        throw new StandardException("can't apply NOT operation to non-boolean variable.");
    }

    @Override
    public IType typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        if (exprToEvaluate.typecheck(typeEnv).equals(new BoolType()))
            return new BoolType();
        throw new StandardException("operand is not a boolean.");
    }

    @Override
    public String toString() {
        return "not " + exprToEvaluate.toString();
    }
}
