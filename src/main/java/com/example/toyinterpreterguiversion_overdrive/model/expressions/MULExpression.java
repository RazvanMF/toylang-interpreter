package com.example.toyinterpreterguiversion_overdrive.model.expressions;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.types.IntType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.IntValue;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.heap.HeapADT;

public class MULExpression implements IExpression {
    IExpression lhs, rhs;

    public MULExpression(IExpression lhs, IExpression rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public IValue evaluate(DictionaryADT<String, IValue> table, HeapADT<IValue> heap) throws StandardException {
        IValue res1 = lhs.evaluate(table, heap);
        IValue res2 = rhs.evaluate(table, heap);

        if (res1.getType().equals(new IntType())) {
            IntValue resInt1 = (IntValue) res1;
            if (res2.getType().equals(new IntType())) {
                IntValue resInt2 = (IntValue) res2;

                //keep it simple
                int parenthesis1 = resInt1.getValue() * resInt2.getValue();
                int parenthesis2 = resInt1.getValue() + resInt2.getValue();
                int result = parenthesis1 - parenthesis2;
                return new IntValue(result);
            }
            throw new StandardException("second operand is not an integer");
        }
        else throw new StandardException("first operand is not an integer");
    }

    @Override
    public IType typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        return new IntType();
    }

    @Override
    public String toString() {
        return "MUL(" + lhs + ", " + rhs + ")";
    }
}
