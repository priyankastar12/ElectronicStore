package com.bikkadit.electronic.store.exception;

import lombok.Builder;

public class ResourceNotFoundException  extends RuntimeException{

    @Builder
    public ResourceNotFoundException(){
        super("Resource not found !!");
    }
    public ResourceNotFoundException(String message){
        super(message);
    }
}
