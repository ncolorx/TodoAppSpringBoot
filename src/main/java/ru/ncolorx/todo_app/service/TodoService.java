package ru.ncolorx.todo_app.service;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ncolorx.todo_app.dto.CreateTodoRequest;
import ru.ncolorx.todo_app.dto.TodoResponse;
import ru.ncolorx.todo_app.dto.UpdateTodoRequest;
import ru.ncolorx.todo_app.exceptions.*;
import ru.ncolorx.todo_app.entity.TodoEntity;
import ru.ncolorx.todo_app.enums.Status;
import ru.ncolorx.todo_app.mapper.TodoMapper;
import ru.ncolorx.todo_app.repository.TodoRepository;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class TodoService {
    private final TodoRepository repository;
    private final TodoMapper mapper;

    @Autowired
    public TodoService(TodoRepository repository, TodoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public TodoResponse createTask(CreateTodoRequest request) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dueDateTime = request.getDueDate().atStartOfDay();
        if (dueDateTime.isBefore(now)) {
            throw new InvalidTodoException("Дата дедлайна раньше, чем дата создания задачи!");
        }
        if (request.getTitle().isBlank() || request.getDescription() == null) {
            throw new InvalidTodoException("Название не может быть пустым!");
        }

        TodoEntity todoEntity = mapper.fromCreateTaskRequest(request);
        todoEntity.setCreatedAt(LocalDateTime.now());

        TodoEntity saved = repository.save(todoEntity);
        return mapper.toResponse(saved);
    }


    public List<TodoResponse> getAllTasks() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    public List<TodoResponse> getTasksByStatus(Status status) {
        return repository.findByStatus(status).stream().map(mapper::toResponse).toList();
    }

    @Transactional
    public TodoResponse updateTaskById(Long id, UpdateTodoRequest request) {

        TodoEntity todoEntity = repository.findById(id).orElseThrow(() -> new TodoNotFoundException(id));
        if (todoEntity.getStatus() == Status.DONE) {
            throw new TodoAlreadyCompletedException(id);
        }
        mapper.fromUpdateTaskRequest(request, todoEntity);
        TodoEntity updated = repository.save(todoEntity);
        return mapper.toResponse(updated);
    }

    public TodoResponse findTaskById(Long id) {
        TodoEntity todoEntity = repository.findById(id).orElseThrow(() -> new TodoNotFoundException(id));
        return mapper.toResponse(todoEntity);
    }

    @Transactional
    public void deleteTaskById(Long id) {
        repository.deleteById(id);
    }


    public List<TodoResponse> getTasksOrderBy(String order) {
        return switch (order) {
            case "dueDate" -> repository.findAllByOrderByDueDate().stream().map(mapper::toResponse).toList();
            case "status" -> repository.findAllByOrderByStatus().stream().map(mapper::toResponse).toList();
            default -> throw new IllegalArgumentException("Неизвестная сортировка: " + order);
        };
    }
}
