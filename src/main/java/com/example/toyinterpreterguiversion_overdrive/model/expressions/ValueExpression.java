package com.example.toyinterpreterguiversion_overdrive.model.expressions;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.heap.HeapADT;

public class ValueExpression implements IExpression {
    IValue value;
    public ValueExpression(IValue val) {
        value = val;
    }
    @Override
    public IValue evaluate(DictionaryADT<String, IValue> table, HeapADT<IValue> heap) {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public IType typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        return value.getType();
    }
}
