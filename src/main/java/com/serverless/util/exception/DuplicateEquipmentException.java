package com.serverless.util.exception;

public class DuplicateEquipmentException extends IllegalArgumentException {
    public DuplicateEquipmentException(String message) { super(message); }
}