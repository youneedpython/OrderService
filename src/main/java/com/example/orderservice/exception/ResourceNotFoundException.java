package com.example.orderservice.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException of(String resourceName, Long id) {
        return new ResourceNotFoundException(resourceName + " ID(" + id + ")를 찾을 수 없습니다.");
    }
}
