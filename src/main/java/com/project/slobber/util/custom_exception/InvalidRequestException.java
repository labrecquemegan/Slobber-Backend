package com.project.slobber.util.custom_exception;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(){}

    public InvalidRequestException(String message){
        super(message);
    }
}
