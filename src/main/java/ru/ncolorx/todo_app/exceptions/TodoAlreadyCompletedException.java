package ru.ncolorx.todo_app.exceptions;

public class TodoAlreadyCompletedException extends RuntimeException {
    public TodoAlreadyCompletedException(Long id) {

        super("Задача с id: " + id + " была уже выполнена и не может быть изменена.");
    }
}
