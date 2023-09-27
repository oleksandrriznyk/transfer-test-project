package com.riznyk.transfer.controller;

import com.riznyk.transfer.dto.ErrorResponse;
import com.riznyk.transfer.exception.AccountNotFoundException;
import com.riznyk.transfer.exception.InsufficientBalanceException;
import com.riznyk.transfer.exception.SameAccountTransferException;
import com.riznyk.transfer.exception.TransferFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(AccountNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return createResponseEntityFromErrorResponse(error);
    }

    @ExceptionHandler(value = {InsufficientBalanceException.class, SameAccountTransferException.class, TransferFailedException.class})
    public ResponseEntity<ErrorResponse> handleValidationException(Exception ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
        return createResponseEntityFromErrorResponse(error);
    }

    private ResponseEntity<ErrorResponse> createResponseEntityFromErrorResponse(ErrorResponse errorResponse) {
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }

}
