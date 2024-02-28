package com.example.toyinterpreterguiversion_overdrive.model.expressions;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.types.BoolType;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.types.IntType;
import com.example.toyinterpreterguiversion_overdrive.model.values.BoolValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.IntValue;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.heap.HeapADT;


public class RelationalExpression implements IExpression {

    String relation;
    IExpression lefthandside, righthandside;

    public RelationalExpression(String rel, IExpression lhs, IExpression rhs) {
        relation = rel; lefthandside = lhs; righthandside = rhs;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", lefthandside.toString(), relation, righthandside.toString());
    }
    @Override
    public IValue evaluate(DictionaryADT<String, IValue> table, HeapADT<IValue> heap) throws StandardException {
        //returns boolValue true or false
        IValue lhv = lefthandside.evaluate(table, heap);
        IValue rhv = righthandside.evaluate(table, heap);
        if (lhv instanceof IntValue && rhv instanceof IntValue)
        {
            int lhvConvert = ((IntValue) lhv).getValue();
            int rhvConvert = ((IntValue) rhv).getValue();
            return switch(relation) {
                case "<":
                    yield new BoolValue(lhvConvert < rhvConvert);
                case ">":
                    yield new BoolValue(lhvConvert > rhvConvert);
                case "<=":
                    yield new BoolValue(lhvConvert <= rhvConvert);
                case ">=":
                    yield new BoolValue(lhvConvert >= rhvConvert);
                case "==":
                    yield new BoolValue(lhvConvert == rhvConvert);
                case "!=":
                    yield new BoolValue(lhvConvert != rhvConvert);
                default:
                    throw new StandardException(String.format("Operator %s not expected.", relation));
            };
        }
        else
            throw new StandardException(String.format("Unexpected component values: %s, %s",
                    lhv.getType().toString(), rhv.getType().toString()));
    }

    @Override
    public IType typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        IType typ1, typ2;
        typ1 = lefthandside.typecheck(typeEnv);
        typ2 = righthandside.typecheck(typeEnv);
        if (typ1.equals(new IntType())) {
            if (typ2.equals(new IntType())) {
                return new BoolType();
            }
            else
                throw new StandardException("right hand side operand is not an integer");
        }
        else
            throw new StandardException("left hand side operand is not an integer");
    }
}
