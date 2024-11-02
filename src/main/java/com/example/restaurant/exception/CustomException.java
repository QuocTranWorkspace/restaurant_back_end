package com.example.restaurant.exception;

/**
 * The type Custom exception.
 */
public class CustomException extends IllegalArgumentException {
    /**
     * Instantiates a new Custom exception.
     *
     * @param message the message
     */
    public CustomException(String message) {
        super(message);
    }
}
