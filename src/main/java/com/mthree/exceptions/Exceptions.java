package com.mthree.exceptions;

import org.springframework.stereotype.Component;

/**
 * Application implementation of exceptions
 */
@Component
public class Exceptions {

    public static class DaoException extends RuntimeException {
        public DaoException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
