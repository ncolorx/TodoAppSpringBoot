package ru.ncolorx.todo_app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.ncolorx.todo_app.controller.TodoController;
import ru.ncolorx.todo_app.dto.CreateTodoRequest;
import ru.ncolorx.todo_app.dto.TodoResponse;
import ru.ncolorx.todo_app.service.TodoService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateTask() throws Exception {
        CreateTodoRequest request = new CreateTodoRequest();
        request.setTitle("Controller Task");
        request.setDescription("desc");
        request.setDueDate(LocalDate.now().plusDays(1));

        TodoResponse response = new TodoResponse();
        response.setId(1L);
        response.setTitle("Controller Task");

        when(service.createTask(any(CreateTodoRequest.class))).thenReturn(response);

        mockMvc.perform(post("/task/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Controller Task"));
    }

    @Test
    void testGetAllTasks() throws Exception {
        TodoResponse t = new TodoResponse();
        t.setId(1L);
        t.setTitle("Task1");

        when(service.getAllTasks()).thenReturn(List.of(t));

        mockMvc.perform(get("/task/getALlTasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Task1"));
    }
}
