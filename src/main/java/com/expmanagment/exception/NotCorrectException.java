package com.expmanagment.exception;

/**
 * 密码错误异常
 */
public class NotCorrectException extends RuntimeException {
    public NotCorrectException(String message) {
        super(message);
    }
}