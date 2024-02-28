package com.example.toyinterpreterguiversion_overdrive.utils.stack;

import java.util.List;

public interface IStack<T> {
    T pop();
    void push(T value);
    boolean isEmpty();
    List<T> getReversed();
}
