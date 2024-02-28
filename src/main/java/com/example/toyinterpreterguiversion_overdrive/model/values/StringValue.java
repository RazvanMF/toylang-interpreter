package com.example.toyinterpreterguiversion_overdrive.model.values;

import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.types.StringType;

public class StringValue implements IValue {
    String value;

    public String getValue() {
        return value;
    }

    public StringValue(String v) {
        value = v;
    }
    @Override
    public IType getType() {
        return new StringType();
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object rhs) {
        if (rhs instanceof StringValue)
            return value.equals(((StringValue) rhs).getValue());
        return false;
    }
}
