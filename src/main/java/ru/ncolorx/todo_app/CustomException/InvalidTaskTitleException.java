package ru.ncolorx.todo_app.CustomException;

public class InvalidTaskTitleException extends RuntimeException {
    public InvalidTaskTitleException(String message) {
        super(message);
    }
}
