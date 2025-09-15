package ru.ncolorx.todo_app.exceptions;

public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException(Long id) {
        super("Задачи с id " + id + " не найдено!");
    }
}
