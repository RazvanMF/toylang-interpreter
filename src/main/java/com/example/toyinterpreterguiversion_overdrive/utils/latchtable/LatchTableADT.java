package com.example.toyinterpreterguiversion_overdrive.utils.latchtable;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.utils.heap.IHeap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LatchTableADT<T> implements IHeap<T> {
    static int currentAddress = 0;
    Map<Integer, T> latches = Collections.synchronizedMap(new HashMap<Integer, T>());
    //to fit w/ synchronizedunion aspect?, if it behaves unexpectedly, change back to ... = new HashMap<>();

    @Override
    public T lookup(int addr) {
        return latches.get(addr);
    }

    @Override
    public boolean isDefined(int addr) {
        return latches.containsKey(addr);
    }

    @Override
    public int put(T value) {
        latches.put(currentAddress, value);
        currentAddress++;
        return currentAddress - 1;
    }

    @Override
    public void update(int addr, T value) {
        latches.put(addr, value);
    }

    @Override
    public void remove(int addr) throws StandardException {
        throw new StandardException("not yet implemented?");
    }

    public LatchTableADT<T> deepcopy() {
        LatchTableADT<T> copy = new LatchTableADT<T>();
        latches.forEach((Int, T) -> copy.put(T));
        return copy;
    }

    public Map<Integer, T> getDictionary() {
        return latches;
    }
}
