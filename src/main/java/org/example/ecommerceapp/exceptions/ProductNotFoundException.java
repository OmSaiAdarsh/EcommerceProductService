package org.example.ecommerceapp.exceptions;

public class ProductNotFoundException extends Exception
{
    public ProductNotFoundException(String message)
    {
        super(message);
    }
}