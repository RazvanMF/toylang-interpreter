package com.example.toyinterpreterguiversion_overdrive.model.expressions;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.heap.HeapADT;

public class VariableLookupExpression implements IExpression{
    String name;
    public VariableLookupExpression(String id) {
        name = id;
    }
    @Override
    public IValue evaluate(DictionaryADT<String, IValue> table, HeapADT<IValue> heap) throws StandardException {
        if (table.isDefined(name))
            return table.lookup(name);
        throw new StandardException(String.format("Variable name %s not declared.", name));
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public IType typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        return typeEnv.lookup(name);
    }

}
