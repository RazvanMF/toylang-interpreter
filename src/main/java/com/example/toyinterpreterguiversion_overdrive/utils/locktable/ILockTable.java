package com.example.toyinterpreterguiversion_overdrive.utils.locktable;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;

public interface ILockTable<T> {
    T lookup(int addr);
    boolean isDefined(int addr);
    int put(T value);
    void update(int addr, T value);
    void remove(int addr) throws StandardException;
}
