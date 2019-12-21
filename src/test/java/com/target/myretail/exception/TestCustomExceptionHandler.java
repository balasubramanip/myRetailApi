package com.target.myretail.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.target.myretail.service.model.ErrorMessage;

@RunWith(SpringJUnit4ClassRunner.class)
public class TestCustomExceptionHandler {
    @Mock
    MethodArgumentTypeMismatchException methodArgumentTypeMismatchException;

    @Test
    public void testHandleArgTypeMiExceptionHandler() {
        ExceptionControllerAdvice customExceptionHandler = new ExceptionControllerAdvice();
        ResponseEntity<ErrorMessage> response = customExceptionHandler.handleArgTypeMiExceptionHandler(methodArgumentTypeMismatchException);
        assertEquals(HttpStatus.BAD_REQUEST,
                     response.getStatusCode());
    }

    @Test
    public void testHandleException() {
        ExceptionControllerAdvice customExceptionHandler = new ExceptionControllerAdvice();
        ResponseEntity<ErrorMessage> response = customExceptionHandler.handleGenericException(new Exception());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,
                     response.getStatusCode());
    }

    @Test
    public void testProductNotFoundException() {
        ExceptionControllerAdvice customExceptionHandler = new ExceptionControllerAdvice();
        ResponseEntity<ErrorMessage> response = customExceptionHandler.productNotFoundException(new ProductNotFoundException());
        assertEquals(HttpStatus.NOT_FOUND,
                     response.getStatusCode());
    }

    @Test
    public void testProductException() {
        ExceptionControllerAdvice customExceptionHandler = new ExceptionControllerAdvice();
        ResponseEntity<ErrorMessage> response = customExceptionHandler.handleProductException(new ProductException("Error Message"));
        assertEquals(HttpStatus.BAD_REQUEST,
                     response.getStatusCode());
    }

    @Test
    public void testClientServerException() {
        ExceptionControllerAdvice customExceptionHandler = new ExceptionControllerAdvice();
        ResponseEntity<ErrorMessage> response = customExceptionHandler.handleClientServerException(new ClientServerException("Error Message",
                                                                                                                            HttpStatus.NO_CONTENT));
        assertEquals(HttpStatus.NO_CONTENT,
                     response.getStatusCode());
    }
}
