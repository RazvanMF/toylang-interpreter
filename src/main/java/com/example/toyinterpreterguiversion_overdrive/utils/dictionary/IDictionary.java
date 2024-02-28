package com.example.toyinterpreterguiversion_overdrive.utils.dictionary;

public interface IDictionary<Key, Value> {
    Value lookup(Key k);
    boolean isDefined(Key k);
    void put(Key k, Value v);
    void update(Key k, Value v);
    void remove(Key k);

}
