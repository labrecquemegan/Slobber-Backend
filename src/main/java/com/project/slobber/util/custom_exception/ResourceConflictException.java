package com.project.slobber.util.custom_exception;

public class ResourceConflictException extends RuntimeException {
    public ResourceConflictException(){}

    public ResourceConflictException(String message){
        super(message);
    }
}
