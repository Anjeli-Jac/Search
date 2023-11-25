/*
 * Copyright (c) 2022 Ian Clement. All rights reserved.
 */

package ca.qc.johnabbott.cs4p6.collections;

/**
 * Exception thrown when a queue overflows.
 *
 * @author Ian Clement
 */
public class QueueOverflowException extends RuntimeException {

    public QueueOverflowException() {
        super();
    }

    public QueueOverflowException(String message) {
        super(message);
    }
}
