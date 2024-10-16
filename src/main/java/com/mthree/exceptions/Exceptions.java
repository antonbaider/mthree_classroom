package com.mthree.exceptions;

public class Exceptions {

    public static class DaoException extends RuntimeException {
        public DaoException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
