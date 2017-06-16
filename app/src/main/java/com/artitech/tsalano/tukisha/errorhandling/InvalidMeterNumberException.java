package com.artitech.tsalano.tukisha.errorhandling;

/**
 * Created by solly on 2017/06/06.
 */

public class InvalidMeterNumberException extends Exception {
    String message;

    public InvalidMeterNumberException() {
        super();
    }

    public InvalidMeterNumberException(String message) {
        super(message);

        this.message = message;
    }
}
