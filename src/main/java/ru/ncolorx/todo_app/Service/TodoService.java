package ru.ncolorx.todo_app.Service;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ncolorx.todo_app.CustomException.InvalidDueDateException;
import ru.ncolorx.todo_app.CustomException.InvalidTaskTitleException;
import ru.ncolorx.todo_app.CustomException.TaskAlreadyCompletedException;
import ru.ncolorx.todo_app.CustomException.TaskNotFoundException;
import ru.ncolorx.todo_app.DTO.CreateTaskRequest;
import ru.ncolorx.todo_app.DTO.TaskResponse;
import ru.ncolorx.todo_app.DTO.UpdateTaskRequest;
import ru.ncolorx.todo_app.Entity.TodoEntity;
import ru.ncolorx.todo_app.Enum.Status;
import ru.ncolorx.todo_app.Mapper.TodoMapper;
import ru.ncolorx.todo_app.Repository.TodoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class TodoService {
    private final TodoRepository repository;
    private final TodoMapper mapper;

    @Autowired
    public TodoService(TodoRepository repository, TodoMapper mapper){
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public TaskResponse createTask (CreateTaskRequest request){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dueDateTime = request.getDueDate().atStartOfDay();
        if(dueDateTime.isBefore(now)){
            throw new InvalidDueDateException("Дата дедлайна раньше, чем дата создания задачи!");
        }
        if(request.getTitle().isBlank() || request.getDescription() == null){
            throw new InvalidTaskTitleException("Название не может быть пустым!");
        }

        TodoEntity todoEntity = mapper.fromCreateTaskRequest(request);
        todoEntity.setCreatedAt(LocalDateTime.now());

        TodoEntity saved = repository.save(todoEntity);
        return mapper.toResponse(saved);
    }


    public List<TaskResponse> getAllTasks(){
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    public List<TaskResponse> getTasksByStatus(Status status){
        return repository.findByStatus(status).stream().map(mapper::toResponse).toList();
    }

    @Transactional
    public TaskResponse updateTaskById(Long id, UpdateTaskRequest request){

        TodoEntity todoEntity = repository.findById(id).orElseThrow(() ->  new TaskNotFoundException(id));
        if(todoEntity.getStatus() == Status.DONE){
            throw new TaskAlreadyCompletedException("Вы не можете изменить уже выполненную задачу!");
        }
        mapper.fromUpdateTaskRequest(request, todoEntity);
        TodoEntity updated = repository.save(todoEntity);
        return mapper.toResponse(updated);
    }

    public TaskResponse findTaskById(Long id){
        TodoEntity todoEntity = repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        return mapper.toResponse(todoEntity);
    }

    @Transactional
    public void deleteTaskById(Long id){
       if(!repository.existsById(id)){
           throw new TaskNotFoundException(id);
       }
       repository.deleteById(id);
    }

    public List<TaskResponse> getAllTasksByOrderByDueDate(){
        return repository.findAllByOrderByDueDate().stream().map(mapper::toResponse).toList();
    }

    public List<TaskResponse> getAllTasksByOrderByStatus(){
        return repository.findAllByOrderByStatus().stream().map(mapper::toResponse).toList();
    }



}
