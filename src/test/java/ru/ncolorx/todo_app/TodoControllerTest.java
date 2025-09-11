package ru.ncolorx.todo_app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.ncolorx.todo_app.Controller.TodoController;
import ru.ncolorx.todo_app.DTO.CreateTaskRequest;
import ru.ncolorx.todo_app.DTO.TaskResponse;
import ru.ncolorx.todo_app.Service.TodoService;

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
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Controller Task");
        request.setDescription("desc");
        request.setDueDate(LocalDate.now().plusDays(1));

        TaskResponse response = new TaskResponse();
        response.setId(1L);
        response.setTitle("Controller Task");

        when(service.createTask(any(CreateTaskRequest.class))).thenReturn(response);

        mockMvc.perform(post("/task/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Controller Task"));
    }

    @Test
    void testGetAllTasks() throws Exception {
        TaskResponse t = new TaskResponse();
        t.setId(1L);
        t.setTitle("Task1");

        when(service.getAllTasks()).thenReturn(List.of(t));

        mockMvc.perform(get("/task/getALlTasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Task1"));
    }
}
