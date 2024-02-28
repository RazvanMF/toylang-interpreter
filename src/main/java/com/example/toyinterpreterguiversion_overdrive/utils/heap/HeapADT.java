package com.example.toyinterpreterguiversion_overdrive.utils.heap;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;

import java.util.HashMap;
import java.util.Map;

public class HeapADT<T> implements IHeap<T>{
    static int currentAddress = 1;
    Map<Integer, T> heap = new HashMap<>();

    @Override
    public T lookup(int addr) {
        return heap.get(addr);
    }

    @Override
    public boolean isDefined(int addr) {
        return heap.containsKey(addr);
    }

    @Override
    public int put(T value) {
        heap.put(currentAddress, value);
        currentAddress++;
        return currentAddress - 1;
    }

    @Override
    public void update(int addr, T value) {
        heap.put(addr, value);
    }

    @Override
    public void remove(int addr) throws StandardException {
        throw new StandardException("not yet implemented?");
    }

    public void setHeap(Map<Integer, T> newHeap) {
        this.heap = newHeap;
    }
    public Map<Integer, T> returnHeap() {
        return heap;
    }

    public HeapADT<T> deepcopy() {
        HeapADT<T> copy = new HeapADT<T>();
        heap.forEach((Int, T) -> copy.put(T));
        return copy;
    }
}
