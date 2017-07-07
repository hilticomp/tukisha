package com.artitech.tsalano.tukisha.errorhandling;

/**
 * Created by solly on 2017/06/06.
 */

public class MeterNumberNotRegisteredException extends Exception {
    String message;

    public MeterNumberNotRegisteredException() {
        super();
    }

    public MeterNumberNotRegisteredException(String message) {
        super(message);

        this.message = message;
    }
}
