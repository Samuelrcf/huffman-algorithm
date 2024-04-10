package com.filescompressorgroup.files_compressor.datastructures.stackusingarray;

public interface MyStackInterface<E> {
    void push(E item);

    E pop();

    E peek();

    boolean isEmpty();

    boolean isFull();

    void show();
} 