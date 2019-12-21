package com.target.myretail.exception;

import com.target.myretail.service.model.ErrorMessage;

public final class ExceptionMessageBuilder {

    private ExceptionMessageBuilder() {
    }

    public static ErrorMessage buildErrorMessage(
            final String errorCode,
            final String message) {
        return new ErrorMessage(errorCode,message);
    }
}
