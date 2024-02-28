package com.example.toyinterpreterguiversion_overdrive.model.values;

import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.types.ReferenceType;

public class ReferenceValue implements IValue {
    int address;
    IType locationType;

    public ReferenceValue(int addr, IType locTy) {
        address = addr;
        locationType = locTy;
    }

    public int getAddress() {
        return address;
    }

    public IType getLocationType() {return locationType;}

    @Override
    public boolean equals(Object rhs) {
        if (rhs instanceof ReferenceValue)
            return (address == ((ReferenceValue) rhs).getAddress()) &&
                    (locationType.equals(((ReferenceValue) rhs).locationType));
        return false;
    }

    @Override
    public IType getType() {
        return new ReferenceType(locationType);
    }

    @Override
    public String toString() {
        return String.format("Address %s -> %s", String.valueOf(address), locationType.toString());
    }
}
