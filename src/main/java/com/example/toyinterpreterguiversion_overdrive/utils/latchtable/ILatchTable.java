package com.example.toyinterpreterguiversion_overdrive.utils.latchtable;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;

public interface ILatchTable<T> {
    T lookup(int addr);
    boolean isDefined(int addr);
    int put(T value);
    void update(int addr, T value);
    void remove(int addr) throws StandardException;
}
