package com.target.myretail.exception;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.target.myretail.service.model.ErrorMessage;
import com.target.myretail.utils.ProductConstant;

@ControllerAdvice
public class ExceptionControllerAdvice {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
    private static final String EXCEPTION_HTTP_STATUS_MESSAGE = "Exception={}, HttpStatus={}, Message={}";

    @ExceptionHandler(ClientServerException.class)
    public ResponseEntity<ErrorMessage> handleClientServerException(final ClientServerException ex) {
        LOG.error(EXCEPTION_HTTP_STATUS_MESSAGE,
                  ex.getClass()
                    .getSimpleName(),
                  ex.getHttpStatus(),
                  ExceptionUtils.getFullStackTrace(ex));
        return new ResponseEntity<>(ExceptionMessageBuilder.buildErrorMessage(ex.getHttpStatus().toString(),ex.getMessage()),
                                    ex.getHttpStatus());

    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorMessage> handleProductException(final ProductException ex) {
        LOG.error(EXCEPTION_HTTP_STATUS_MESSAGE,
                  ex.getClass()
                    .getSimpleName(),
                  HttpStatus.BAD_REQUEST,
                  ExceptionUtils.getFullStackTrace(ex));
        return new ResponseEntity<>(ExceptionMessageBuilder.buildErrorMessage(HttpStatus.BAD_REQUEST.toString(),ex.getMessage()),
                                    HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> handleGenericException(final Exception ex) {

        LOG.error(EXCEPTION_HTTP_STATUS_MESSAGE,
                  ex.getClass()
                    .getSimpleName(),
                  HttpStatus.INTERNAL_SERVER_ERROR,
                  ExceptionUtils.getFullStackTrace(ex));
        return new ResponseEntity<>(ExceptionMessageBuilder.buildErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.toString(),ex.getMessage()),
                                    HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<ErrorMessage> handleArgTypeMiExceptionHandler(MethodArgumentTypeMismatchException ex) {
        LOG.error(EXCEPTION_HTTP_STATUS_MESSAGE,
                  ex.getClass()
                    .getSimpleName(),
                  HttpStatus.BAD_REQUEST,
                  ExceptionUtils.getFullStackTrace(ex));
        return new ResponseEntity<>(ExceptionMessageBuilder.buildErrorMessage(HttpStatus.BAD_REQUEST.toString(),"Invalid ProductInfo Id"),
                                    HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public final ResponseEntity<ErrorMessage> productNotFoundException(ProductNotFoundException ex) {
        LOG.error(EXCEPTION_HTTP_STATUS_MESSAGE,
                  ex.getClass()
                    .getSimpleName(),
                  HttpStatus.NOT_FOUND,
                  ExceptionUtils.getFullStackTrace(ex));
        return new ResponseEntity<>(ExceptionMessageBuilder.buildErrorMessage(HttpStatus.NOT_FOUND.toString(),ProductConstant.PRODUCT_NOT_FOUND),
                                    HttpStatus.NOT_FOUND);
    }

}
