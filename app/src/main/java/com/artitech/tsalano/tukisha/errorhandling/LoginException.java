package com.artitech.tsalano.tukisha.errorhandling;

/**
 * Created by solly on 2017/06/06.
 */

public class LoginException extends Exception {

    String message;

    public LoginException() {
        super();
    }

    public LoginException(String message) {
        super(message);

        this.message = message;
    }
}
