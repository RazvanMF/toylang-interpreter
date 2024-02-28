package com.example.toyinterpreterguiversion_overdrive.model.expressions;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.types.ReferenceType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.ReferenceValue;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.heap.HeapADT;

public class HeapReadingExpression implements IExpression {
    IExpression expression;

    public HeapReadingExpression(IExpression expr) {
        expression = expr;
    }


    @Override
    public IValue evaluate(DictionaryADT<String, IValue> table, HeapADT<IValue> heap) throws StandardException {
        if (expression.evaluate(table, heap).getType() instanceof ReferenceType) {
            int address = ((ReferenceValue) expression.evaluate(table, heap)).getAddress();
            if (heap.isDefined(address)) {
                return heap.lookup(address);
            }
            else throw new StandardException("this is not in heap");
        }
        else throw new StandardException("not a reference type");
    }

    @Override
    public String toString() {
        return String.format("Read from heap at %s", expression.toString());
    }

    @Override
    public IType typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        IType typ = expression.typecheck(typeEnv);
        if (typ instanceof ReferenceType) {
            ReferenceType reft =(ReferenceType) typ;
            return reft.getReferenced();
        } else
            throw new StandardException("the rH expression call argument is not a ReferenceType");
    }
}
