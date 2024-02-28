package com.example.toyinterpreterguiversion_overdrive.utils.barriertable;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.utils.heap.IHeap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BarrierTableADT<T> implements IHeap<T> {
    static int currentAddress = 0;
    Map<Integer, T> barrTable = Collections.synchronizedMap(new HashMap<Integer, T>());
    //to fit w/ synchronizedunion aspect?, if it behaves unexpectedly, change back to ... = new HashMap<>();

    @Override
    public T lookup(int addr) {
        return barrTable.get(addr);
    }

    @Override
    public boolean isDefined(int addr) {
        return barrTable.containsKey(addr);
    }

    @Override
    public int put(T tuple) {
        barrTable.put(currentAddress, tuple);
        currentAddress++;
        return currentAddress - 1;
    }

    @Override
    public void update(int addr, T tuple) {
        barrTable.put(addr, tuple);
    }

    @Override
    public void remove(int addr) throws StandardException {
        throw new StandardException("not yet implemented?");
    }

    public Map<Integer, T> getDictionary() {
        return barrTable;
    }

    public BarrierTableADT<T> deepcopy() {
        BarrierTableADT<T> copy = new BarrierTableADT<T>();
        barrTable.forEach((Int, T) -> copy.put(T));
        return copy;
    }
}
