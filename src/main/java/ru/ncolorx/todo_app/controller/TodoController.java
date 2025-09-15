package ru.ncolorx.todo_app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ncolorx.todo_app.dto.CreateTodoRequest;
import ru.ncolorx.todo_app.dto.TodoResponse;
import ru.ncolorx.todo_app.dto.UpdateTodoRequest;
import ru.ncolorx.todo_app.enums.Status;
import ru.ncolorx.todo_app.service.TodoService;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TodoController {
    private final TodoService service;

    @Autowired
    public TodoController(TodoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TodoResponse> createTask(@RequestBody CreateTodoRequest createTodoRequest) {
        TodoResponse todoResponse = service.createTask(createTodoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(todoResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoResponse> updateTask(@RequestBody UpdateTodoRequest updateTodoRequest, @PathVariable Long id) {
        TodoResponse todoResponse = service.updateTaskById(id, updateTodoRequest);
        return ResponseEntity.status(HttpStatus.OK).body(todoResponse);
    }

    @GetMapping
    public ResponseEntity<List<TodoResponse>> getAllTasks() {
        List<TodoResponse> tasks = service.getAllTasks();
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @GetMapping("/getTasksByStatus")
    public ResponseEntity<List<TodoResponse>> getTasksByStatus(@RequestParam Status status) {
        List<TodoResponse> tasks = service.getTasksByStatus(status);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoResponse> getTaskById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findTaskById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        service.deleteTaskById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Была удалена задача с id: " + id);
    }


    @GetMapping
    public ResponseEntity<List<TodoResponse>> getTasksOrderBy(@RequestParam String order) {
        List<TodoResponse> tasks = service.getTasksOrderBy(order);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }


}
