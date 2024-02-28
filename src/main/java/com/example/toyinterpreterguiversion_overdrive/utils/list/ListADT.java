package com.example.toyinterpreterguiversion_overdrive.utils.list;

import java.util.ArrayList;
import java.util.List;

public class ListADT<T> implements IList<T>{
    private List<T> list;
    public ListADT(){
        list = new ArrayList<>();
    }

    @Override
    public void add(T value) {
        list.add(value);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public String toString() {
        return "Output{list =" + list.toString() + "}";
    }

    public List<T> getList() {
        return list;
    }

    public ListADT<T> deepcopy() {
        ListADT<T> copy = new ListADT<>();
        list.forEach((T) -> copy.add(T));
        return copy;
    }
}
