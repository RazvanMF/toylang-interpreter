package com.example.toyinterpreterguiversion_overdrive.utils.barriertable;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;

public interface IBarrierTable<T> {
    T lookup(int addr);
    boolean isDefined(int addr);
    int put(T tuple);
    void update(int addr, T tuple);
    void remove(int addr) throws StandardException;
}
