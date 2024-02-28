package com.example.toyinterpreterguiversion_overdrive.utils.proceduretable;

import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.IDictionary;

import java.util.HashMap;
import java.util.Map;

public class ProcedureTableADT<Key, Value> implements IDictionary<Key, Value> {
    private Map<Key, Value> map;
    public ProcedureTableADT() {
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
        return "ProcedureTable{dictionary =" + map.toString() + "}";
    }
    public Map<Key, Value> getProcedureTable() {
        return map;
    }

    public ProcedureTableADT<Key, Value> deepcopy() {
        ProcedureTableADT<Key, Value> copy = new ProcedureTableADT<>();
        map.forEach((Key, Value) -> copy.put(Key, Value));
        return copy;
    }


}
