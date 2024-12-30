package com.tasksprints.auction.auction.exception;

public class InvalidAuctionStateException extends RuntimeException {
    public InvalidAuctionStateException(String message) {
        super(message);
    }
}
