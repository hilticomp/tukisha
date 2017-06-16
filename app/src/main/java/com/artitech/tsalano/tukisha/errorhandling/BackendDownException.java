package com.artitech.tsalano.tukisha.errorhandling;

/**
 * Created by solly on 2017/06/06.
 */

public class BackendDownException extends Exception {

    String message;

    public BackendDownException() {
        super();
    }

    public BackendDownException(String message) {
        super(message);

        this.message = message;
    }
}
