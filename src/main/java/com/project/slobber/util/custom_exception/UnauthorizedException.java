package com.project.slobber.util.custom_exception;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(){}

    public UnauthorizedException(String message){
        super(message);
    }
}
