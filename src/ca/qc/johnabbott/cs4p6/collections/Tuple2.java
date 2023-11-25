/*
 * Copyright (c) 2022 Ian Clement. All rights reserved.
 */

package ca.qc.johnabbott.cs4p6.collections;

public class Tuple2<S, T> {

    private S first;
    private T second;

    public Tuple2(S first, T second) {
        this.first = first;
        this.second = second;
    }

    public S getFirst() {
        return first;
    }

    public T getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", first, second);
    }
}
