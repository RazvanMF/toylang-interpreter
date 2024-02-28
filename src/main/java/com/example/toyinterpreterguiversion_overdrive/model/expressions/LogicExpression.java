package com.example.toyinterpreterguiversion_overdrive.model.expressions;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.types.BoolType;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.values.BoolValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.heap.HeapADT;

public class LogicExpression implements IExpression {
    IExpression exp1, exp2;
    String operator;

    public LogicExpression(String op, IExpression e1, IExpression e2) {
        exp1 = e1; exp2 = e2; operator = op.toUpperCase();
    }

    @Override
    public String toString() {
        return String.format("(%s %s %s)", exp1.toString(), operator, exp2.toString());
    }

    @Override
    public IValue evaluate(DictionaryADT<String, IValue> table, HeapADT<IValue> heap) throws StandardException {
        IValue num1 = exp1.evaluate(table, heap);
        if (num1.getType().equals(new BoolType())) {
            IValue num2 = exp2.evaluate(table, heap);
            if (num1.getType().equals(new BoolType())) {
                BoolValue vl1 = (BoolValue)num1;
                BoolValue vl2 = (BoolValue)num2;
                boolean state1, state2;
                state1 = vl1.getValue(); state2 = vl2.getValue();
                boolean result = switch (operator) {
                    case "AND" -> state1 && state2;
                    case "OR" -> state1 || state2;
                    default -> throw new StandardException("Unexpected operator.");
                };
                return new BoolValue(result);
            }
            else
                throw new StandardException("Second expression not a boolean.");
        }
        else
            throw new StandardException("Second expression not a boolean.");
    }

    @Override
    public IType typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        IType typ1, typ2;
        typ1 = exp1.typecheck(typeEnv);
        typ2 = exp2.typecheck(typeEnv);
        if (typ1.equals(new BoolType())) {
            if (typ2.equals(new BoolType())) {
                return new BoolType();
            }
            else
                throw new StandardException("second operand is not a boolean");
        }
        else
            throw new StandardException("first operand is not a boolean");
    }
}
