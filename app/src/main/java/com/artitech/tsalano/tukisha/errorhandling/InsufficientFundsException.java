package com.artitech.tsalano.tukisha.errorhandling;

/**
 * Created by solly on 2018/04/04.
 */

public class InsufficientFundsException extends Exception {

    String message;

    public InsufficientFundsException() {
        super();
    }

    public InsufficientFundsException(String message) {
        super(message);

        this.message = message;
    }
}