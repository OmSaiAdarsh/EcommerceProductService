package org.example.ecommerceapp.exceptions;

import lombok.Data;

@Data
public class UserNotLoggedInException extends Exception {
    public UserNotLoggedInException() {
        super("User is not logged in");
    }
    private int status;
    private String message;
}
