package com.example.toyinterpreterguiversion_overdrive.model.values;

import com.example.toyinterpreterguiversion_overdrive.model.types.BoolType;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;

public class BoolValue implements IValue {
    boolean value;

    public boolean getValue() {
        return value;
    }
    public BoolValue(boolean val) {
        value = val;
    }

    @Override
    public IType getType() {
        return new BoolType();
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object rhs) {
        if (rhs instanceof BoolValue)
            return value == ((BoolValue) rhs).getValue();
        return false;
    }
}
