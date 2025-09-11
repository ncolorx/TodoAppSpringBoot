package ru.ncolorx.todo_app.CustomException;

public class InvalidDueDateException extends RuntimeException {
    public InvalidDueDateException(String message) {
        super(message);
    }
}
