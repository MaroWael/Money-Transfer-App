package com.transfer.exception.custom;

public class CustomerAlreadyExistException extends Exception {
    public CustomerAlreadyExistException(String message) {
        super(message);
    }

}
