/*
 * Copyright (c) 2022 Ian Clement. All rights reserved.
 */

package ca.qc.johnabbott.cs4p6.collections;

/**
 * Get a copy of the object.
 * - A cleaner implementation of cloneable.
 * @param <T>
 */
public interface Copyable<T> {
    /**
     * Get a copy (clone) of the object.
     * @return The copy.
     */
    T copy();
}
