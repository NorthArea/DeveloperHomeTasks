package ru.northarea.exchanger.exception;

public class AppNotCorrectInitializedException extends RuntimeException {
    public AppNotCorrectInitializedException(String message) {
        super(message);
    }
}
