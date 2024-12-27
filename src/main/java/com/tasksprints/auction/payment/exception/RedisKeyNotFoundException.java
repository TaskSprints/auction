package com.tasksprints.auction.payment.exception;

public class RedisKeyNotFoundException extends RuntimeException {
    public RedisKeyNotFoundException(String message) {
        super(message);
    }
}
