package com.groupone.exception;

import org.springframework.security.core.AuthenticationException;

public class AccountActivatedException extends AuthenticationException {

    public AccountActivatedException(String msg) {
        super(msg);
    }
    public AccountActivatedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
