package com.example.toyinterpreterguiversion_overdrive.utils.stack;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class StackADT<T> implements IStack<T> {
    private final Stack<T> stack;
    public StackADT() {
        stack = new Stack<>();
    }

    @Override
    public T pop() {
        return stack.pop();
    }

    @Override
    public void push(T value) {
        stack.push(value);
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public List<T> getReversed() {
        List<T> items;
        items = Arrays.asList((T[])stack.toArray());
        Collections.reverse(items);
        return items;
    }

    public List<T> getAsList() {
        List<T> items;
        items = Arrays.asList((T[])stack.toArray());
        return items;
    }

    @Override
    public String toString() {
        return "ExeStack{stack = " + stack.toString() + "}";
    }

    public StackADT<T> deepcopy() {
        StackADT<T> copy = new StackADT<>();
        List<T> items = getReversed();
        items.forEach((T) -> copy.push(T));
        return copy;
    }

    public int size() {
        return stack.size();
    }

    public T top() {
        return stack.peek();
    }
}
