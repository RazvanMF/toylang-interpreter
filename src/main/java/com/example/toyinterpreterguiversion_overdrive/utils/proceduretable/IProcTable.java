package com.example.toyinterpreterguiversion_overdrive.utils.proceduretable;

public interface IProcTable<Key, Value> {
    Value lookup(Key k);

    boolean isDefined(Key k);
    void put(Key k, Value v);
    void update(Key k, Value v);
    void remove(Key k);

}
