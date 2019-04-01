package com.serverless.util.exception;

public class EquipmentNotFoundException extends IllegalArgumentException {
    public EquipmentNotFoundException(String message) {
        super(message);
    }
}