package ru.ncolorx.todo_app.exceptions;

public class InvalidTodoException extends RuntimeException {
    public InvalidTodoException(String message) {
        super(message);
    }
}
