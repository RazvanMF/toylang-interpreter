package com.example.toyinterpreterguiversion_overdrive.utils.dictionary;

import java.util.HashMap;
import java.util.Map;

public class DictionaryADT<Key, Value> implements IDictionary<Key, Value>{
    private Map<Key, Value> map;
    public DictionaryADT() {
        map = new HashMap<>();
    }

    @Override
    public Value lookup(Key k) {
        return map.get(k);
    }
    @Override
    public boolean isDefined(Key k) {
        return map.containsKey(k);
    }
    @Override
    public void put(Key k, Value v) {
        map.put(k, v);
    }
    @Override
    public void update(Key k, Value v) {
        map.put(k, v);
    }
    @Override
    public void remove(Key k) {
        map.remove(k);
    }
    @Override
    public String toString() {
        return "SymTable{dictionary =" + map.toString() + "}";
    }
    public Map<Key, Value> getDictionary() {
        return map;
    }

    public DictionaryADT<Key, Value> deepcopy() {
        DictionaryADT<Key, Value> copy = new DictionaryADT<>();
        map.forEach((Key, Value) -> copy.put(Key, Value));
        return copy;
    }


}
