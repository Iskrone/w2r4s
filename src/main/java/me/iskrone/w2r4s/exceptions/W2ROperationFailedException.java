package me.iskrone.w2r4s.exceptions;

/**
 * Created by Iskander on 10.03.2021
 */
public class W2ROperationFailedException extends Exception {
    public W2ROperationFailedException(String message) {
        super(message);
    }

    public W2ROperationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
