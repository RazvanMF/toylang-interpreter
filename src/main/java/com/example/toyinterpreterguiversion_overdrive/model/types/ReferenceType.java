package com.example.toyinterpreterguiversion_overdrive.model.types;

import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.ReferenceValue;

public class ReferenceType implements IType {
    IType referenced;
    public ReferenceType (IType in) {
        referenced = in;
    }

    public IType getReferenced() {
        return referenced;
    }

    @Override
    public boolean equals(Object rhs) {
        if (rhs instanceof ReferenceType)
            return referenced.equals(((ReferenceType) rhs).getReferenced());
        else
            return false;
    }
    @Override
    public IValue defaultValue() {
        return new ReferenceValue(0, referenced);
    }

    @Override
    public String toString() {
        return String.format("Reference(%s)", referenced.toString());
    }
}
