package ru.ncolorx.todo_app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import ru.ncolorx.todo_app.Service.TodoService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TodoServiceTest {

    @Mock
    private TodoRepository repository;

    @Mock
    private TodoMapper mapper;

    @InjectMocks
    private TodoService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTaskInvalidDueDate() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Task");
        request.setDescription("desc");
        request.setDueDate(LocalDate.now().minusDays(1));

        assertThrows(InvalidDueDateException.class, () -> service.createTask(request));
    }

    @Test
    void testCreateTaskInvalidTitle() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle(" ");
        request.setDescription(null);
        request.setDueDate(LocalDate.now().plusDays(1));

        assertThrows(InvalidTaskTitleException.class, () -> service.createTask(request));
    }

    @Test
    void testUpdateTaskAlreadyCompleted() {
        TodoEntity entity = new TodoEntity();
        entity.setId(1L);
        entity.setStatus(Status.DONE);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        UpdateTaskRequest request = new UpdateTaskRequest();

        assertThrows(TaskAlreadyCompletedException.class, () -> service.updateTaskById(1L, request));
    }

    @Test
    void testFindTaskByIdNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> service.findTaskById(99L));
    }

    @Test
    void testDeleteTaskNotFound() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(TaskNotFoundException.class, () -> service.deleteTaskById(99L));
    }

    @Test
    void testCreateTaskSuccess() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Valid Task");
        request.setDescription("desc");
        request.setDueDate(LocalDate.now().plusDays(1));

        TodoEntity entity = new TodoEntity();
        entity.setId(1L);
        entity.setTitle("Valid Task");
        entity.setDescription("desc");
        entity.setStatus(Status.TODO);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setDueDate(request.getDueDate());

        when(mapper.fromCreateTaskRequest(request)).thenReturn(entity);
        when(repository.save(any(TodoEntity.class))).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(new TaskResponse());

        TaskResponse response = service.createTask(request);
        assertThat(response).isNotNull();
    }
}
