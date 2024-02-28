package com.example.toyinterpreterguiversion_overdrive.model.types;

import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.IntValue;

public class IntType implements IType {
    @Override
    public boolean equals(Object rhs) {
        if (rhs instanceof IntType)
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "int";
    }

    @Override
    public IValue defaultValue() {
        return new IntValue(0);
    }
}
