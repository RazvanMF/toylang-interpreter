package com.example.toyinterpreterguiversion_overdrive.model.types;

import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;

public interface IType {
    boolean equals(Object rhs);
    String toString();
    IValue defaultValue();

}
