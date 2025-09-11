package ru.ncolorx.todo_app.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ncolorx.todo_app.DTO.CreateTaskRequest;
import ru.ncolorx.todo_app.DTO.TaskResponse;
import ru.ncolorx.todo_app.DTO.UpdateTaskRequest;
import ru.ncolorx.todo_app.Enum.Status;
import ru.ncolorx.todo_app.Service.TodoService;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TodoController {
    private final TodoService service;

    @Autowired
    public TodoController(TodoService service){
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<TaskResponse> createTask(@RequestBody CreateTaskRequest createTaskRequest) {
        TaskResponse taskResponse = service.createTask(createTaskRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskResponse);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TaskResponse> updateTask(@RequestBody UpdateTaskRequest updateTaskRequest, @PathVariable Long id) {
        TaskResponse taskResponse = service.updateTaskById(id, updateTaskRequest);
        return ResponseEntity.status(HttpStatus.OK).body(taskResponse);
    }

    @GetMapping("/getALlTasks")
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        List<TaskResponse> tasks = service.getAllTasks();
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @GetMapping("/getTasksByStatus")
    public ResponseEntity<List<TaskResponse>> getTasksByStatus(@RequestParam Status status) {
        List<TaskResponse> tasks = service.getTasksByStatus(status);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findTaskById(id));
    }

    @DeleteMapping("/delete/{id}")
   public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        service.deleteTaskById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Была удалена задача с id: " + id);
    }

    @GetMapping("/orderByDueDate")
    public ResponseEntity<List<TaskResponse>> getTasksOrderByDueDate() {
        List<TaskResponse> tasks = service.getAllTasksByOrderByDueDate();
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @GetMapping("/orderByStatus")
    public ResponseEntity<List<TaskResponse>> getTasksOrderByStatus() {
        List<TaskResponse> tasks = service.getAllTasksByOrderByStatus();
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }




}
