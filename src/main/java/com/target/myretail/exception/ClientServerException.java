package com.target.myretail.exception;

import org.springframework.http.HttpStatus;

/*
 * This exception will be thrown for all 400 and 500 error
 * */

public class ClientServerException extends Exception {

    private static final long serialVersionUID = 1L;

    private final HttpStatus httpStatus;


    /**
     * Public constructor
     *
     * @param msg
     */
    public ClientServerException(final String msg,
                                 final HttpStatus httpStatus) {
        super(msg);
        this.httpStatus = httpStatus;

    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
