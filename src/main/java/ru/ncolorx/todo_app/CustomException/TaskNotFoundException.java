package ru.ncolorx.todo_app.CustomException;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long id) {
        super("Задачи с id " + id + " не найдено!");
    }
}
