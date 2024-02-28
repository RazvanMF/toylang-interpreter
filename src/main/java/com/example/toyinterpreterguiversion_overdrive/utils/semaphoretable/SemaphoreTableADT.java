package com.example.toyinterpreterguiversion_overdrive.utils.semaphoretable;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.utils.heap.IHeap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SemaphoreTableADT<T> implements IHeap<T> {
    static int currentAddress = 0;
    Map<Integer, T> semTable = Collections.synchronizedMap(new HashMap<Integer, T>());
    //to fit w/ synchronizedunion aspect?, if it behaves unexpectedly, change back to ... = new HashMap<>();

    @Override
    public T lookup(int addr) {
        return semTable.get(addr);
    }

    @Override
    public boolean isDefined(int addr) {
        return semTable.containsKey(addr);
    }

    @Override
    public int put(T tuple) {
        semTable.put(currentAddress, tuple);
        currentAddress++;
        return currentAddress - 1;
    }

    @Override
    public void update(int addr, T tuple) {
        semTable.put(addr, tuple);
    }

    @Override
    public void remove(int addr) throws StandardException {
        throw new StandardException("not yet implemented?");
    }

    public void setSemTable(Map<Integer, T> newSemTable) {
        this.semTable = newSemTable;
    }
    public Map<Integer, T> returnSemTable() {
        return semTable;
    }

    public SemaphoreTableADT<T> deepcopy() {
        SemaphoreTableADT<T> copy = new SemaphoreTableADT<T>();
        semTable.forEach((Int, T) -> copy.put(T));
        return copy;
    }
}
