package ru.ncolorx.todo_app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.ncolorx.todo_app.dto.CreateTodoRequest;
import ru.ncolorx.todo_app.dto.TodoResponse;
import ru.ncolorx.todo_app.dto.UpdateTodoRequest;
import ru.ncolorx.todo_app.entity.TodoEntity;
import ru.ncolorx.todo_app.enums.Status;
import ru.ncolorx.todo_app.exceptions.InvalidTodoException;
import ru.ncolorx.todo_app.exceptions.TodoAlreadyCompletedException;
import ru.ncolorx.todo_app.exceptions.TodoNotFoundException;
import ru.ncolorx.todo_app.mapper.TodoMapper;
import ru.ncolorx.todo_app.repository.TodoRepository;
import ru.ncolorx.todo_app.service.TodoService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TodoServiceTest {

    @Mock
    private TodoRepository repository;

    @Mock
    private TodoMapper mapper;

    @InjectMocks
    private TodoService service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createTaskSuccess() {
        CreateTodoRequest request = new CreateTodoRequest();
        request.setTitle("title");
        request.setDescription("description");
        request.setDueDate(LocalDate.now().plusDays(1));

        TodoEntity entity = new TodoEntity();
        entity.setId(1L);

        TodoResponse response = new TodoResponse();

        when(mapper.fromCreateTaskRequest(request)).thenReturn(entity);
        when(repository.save(any(TodoEntity.class))).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(response);

        TodoResponse result = service.createTask(request);

        assertThat(result).isEqualTo(response);
        verify(repository).save(entity);
    }

    @Test
    void createTaskInvalidTitle() {
        CreateTodoRequest request = new CreateTodoRequest();
        request.setTitle(" ");
        request.setDescription("description");
        request.setDueDate(LocalDate.now().plusDays(1));

        assertThrows(InvalidTodoException.class, () -> service.createTask(request));

    }

    @Test
    void createTaskInvalidDueDate() {
        CreateTodoRequest request = new CreateTodoRequest();
        request.setTitle("title");
        request.setDescription("description");
        request.setDueDate(LocalDate.now().minusDays(1));

        assertThrows(InvalidTodoException.class, () -> service.createTask(request));
    }

    @Test
    void getAllTasksSuccess() {
        TodoEntity entity = new TodoEntity();
        TodoResponse response = new TodoResponse();

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        List<TodoResponse> result = service.getAllTasks();

        assertThat(result).containsExactly(response);
    }

    @Test
    void getTasksByStatusSuccess() {
        TodoEntity entity = new TodoEntity();
        entity.setStatus(Status.TODO);
        TodoResponse response = new TodoResponse();

        when(repository.findByStatus(Status.TODO)).thenReturn(List.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        List<TodoResponse> result = service.getTasksByStatus(Status.TODO);

        assertThat(result).containsExactly(response);
    }

    @Test
    void updateTaskBySuccess() {
        UpdateTodoRequest todoRequest = new UpdateTodoRequest();

        TodoEntity entity = new TodoEntity();
        entity.setId(1L);

        TodoResponse response = new TodoResponse();

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(response);

        TodoResponse result = service.updateTaskById(1L, todoRequest);

        assertThat(result).isEqualTo(response);
        verify(repository).save(entity);

    }

    @Test
    void updateTaskInvalidId() {
        UpdateTodoRequest todoRequest = new UpdateTodoRequest();

        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TodoNotFoundException.class, () -> service.updateTaskById(99L, todoRequest));
    }

    @Test
    void updateTaskInvalidStatus() {
        UpdateTodoRequest todoRequest = new UpdateTodoRequest();

        TodoEntity entity = new TodoEntity();
        entity.setId(1L);
        entity.setStatus(Status.DONE);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        assertThrows(TodoAlreadyCompletedException.class, () -> service.updateTaskById(1L, todoRequest));

    }

    @Test
    void findTaskByIdSuccess() {
        TodoEntity entity = new TodoEntity();
        entity.setId(1L);

        TodoResponse response = new TodoResponse();

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        TodoResponse result = service.findTaskById(1L);

        assertThat(result).isEqualTo(response);
        verify(repository).findById(1L);

    }

    @Test
    void findTaskByIdInvalidId() {

        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TodoNotFoundException.class, () -> service.findTaskById(99L));
    }

    @Test
    void deleteTaskSuccess() {
        service.deleteTaskById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
     void getTaskOrderByStatusSuccess() {
       TodoEntity entity = new TodoEntity();
       TodoResponse response = new TodoResponse();

       when(repository.findAllByOrderByStatus()).thenReturn(List.of(entity));
       when(mapper.toResponse(entity)).thenReturn(response);

       List<TodoResponse> result = service.getTasksOrderBy("status");
       assertThat(result).containsExactly(response);
       verify(repository).findAllByOrderByStatus();
    }

    @Test
    void getTaskOrderByDueDateSuccess() {
        TodoEntity entity  = new TodoEntity();
        TodoResponse response = new TodoResponse();

        when(repository.findAllByOrderByDueDate()).thenReturn(List.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        List<TodoResponse> result = service.getTasksOrderBy("dueDate");
        assertThat(result).containsExactly(response);
        verify(repository).findAllByOrderByDueDate();
    }

    @Test
    void getTaskOrderByInvalid() {
        assertThrows(InvalidTodoException.class, () -> service.getTasksOrderBy("invalid"));
    }


}
