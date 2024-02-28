package com.example.toyinterpreterguiversion_overdrive.model.expressions;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.heap.HeapADT;

public interface IExpression {
    IValue evaluate(DictionaryADT<String, IValue> table, HeapADT<IValue> heap) throws StandardException;

    // expressions work mostly w/ values and types, so the type checker will return a type;
    IType typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException;
}
