package com.example.toyinterpreterguiversion_overdrive.model.values;

import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.types.IntType;

public class IntValue implements IValue {
    int value;

    public int getValue() {
        return value;
    }
    public IntValue(int val) {
        value = val;
    }

    @Override
    public IType getType() {
        return new IntType();
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object rhs) {
        if (rhs instanceof IntValue)
            return value == ((IntValue) rhs).getValue();
        return false;
    }
}
