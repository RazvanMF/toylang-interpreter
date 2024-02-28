package com.example.toyinterpreterguiversion_overdrive.model.types;

import com.example.toyinterpreterguiversion_overdrive.model.values.BoolValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;

public class BoolType implements IType {
    @Override
    public boolean equals(Object rhs) {
        if (rhs instanceof BoolType)
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "bool";
    }

    @Override
    public IValue defaultValue() {
        return new BoolValue(false);
    }
}
