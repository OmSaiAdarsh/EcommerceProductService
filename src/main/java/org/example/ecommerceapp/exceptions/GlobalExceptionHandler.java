package org.example.ecommerceapp.exceptions;

import org.example.ecommerceapp.dtos.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ErrorDTO handleIndexOutOfBoundsException(IndexOutOfBoundsException ex) {
        ErrorDTO error = new ErrorDTO();
        error.setMessage("Index out of bounds");
        error.setStatus("404");
        return error;
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleProductNotFoundException(ProductNotFoundException ex) {
        ErrorDTO error = new ErrorDTO();
        error.setMessage(ex.getMessage());
        error.setStatus("404");

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotLoggedInException.class)
    public ResponseEntity<ErrorDTO> handleUserNotLoggedInException(){
        ErrorDTO error = new ErrorDTO();
        error.setMessage("User not logged in");
        error.setStatus("401");
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<?> handleServerException(Exception ex) {
        ErrorDTO error = new ErrorDTO();
        error.setMessage("Internal Server Error");
        error.setStatus("500");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }




}
