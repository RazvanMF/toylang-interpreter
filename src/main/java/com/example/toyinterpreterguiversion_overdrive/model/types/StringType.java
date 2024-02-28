package com.example.toyinterpreterguiversion_overdrive.model.types;

import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.StringValue;

public class StringType implements IType {

    @Override
    public boolean equals(Object rhs) {
        return rhs instanceof StringType;
    }

    @Override
    public String toString() {
        return "string";
    }

    @Override
    public IValue defaultValue() {
        return new StringValue("");
    }
}
