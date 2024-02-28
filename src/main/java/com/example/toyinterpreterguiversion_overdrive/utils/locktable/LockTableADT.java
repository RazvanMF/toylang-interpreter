package com.example.toyinterpreterguiversion_overdrive.utils.locktable;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.utils.heap.IHeap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LockTableADT<T> implements IHeap<T> {
    static int currentAddress = 0;
    Map<Integer, T> locks = Collections.synchronizedMap(new HashMap<Integer, T>());
    //to fit w/ synchronizedunion aspect?, if it behaves unexpectedly, change back to ... = new HashMap<>();

    @Override
    public T lookup(int addr) {
        return locks.get(addr);
    }

    @Override
    public boolean isDefined(int addr) {
        return locks.containsKey(addr);
    }

    @Override
    public int put(T value) {
        locks.put(currentAddress, value);
        currentAddress++;
        return currentAddress - 1;
    }

    @Override
    public void update(int addr, T value) {
        locks.put(addr, value);
    }

    @Override
    public void remove(int addr) throws StandardException {
        throw new StandardException("not yet implemented?");
    }

    public void setLocks(Map<Integer, T> newHeap) {
        this.locks = newHeap;
    }
    public Map<Integer, T> returnHeap() {
        return locks;
    }

    public LockTableADT<T> deepcopy() {
        LockTableADT<T> copy = new LockTableADT<T>();
        locks.forEach((Int, T) -> copy.put(T));
        return copy;
    }

    public Map<Integer, T> getDictionary() {
        return locks;
    }
}
