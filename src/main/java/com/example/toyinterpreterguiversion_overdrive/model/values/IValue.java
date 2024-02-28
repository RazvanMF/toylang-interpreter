package com.example.toyinterpreterguiversion_overdrive.model.values;

import com.example.toyinterpreterguiversion_overdrive.model.types.IType;

public interface IValue {
    IType getType();
    boolean equals(Object rhs);
    String toString();
}
