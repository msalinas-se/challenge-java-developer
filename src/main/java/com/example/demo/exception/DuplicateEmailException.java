package com.example.demo.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException() {
        super("El correo ya registrado");
    }
}
