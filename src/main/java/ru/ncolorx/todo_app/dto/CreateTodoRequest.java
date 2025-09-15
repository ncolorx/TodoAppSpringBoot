package ru.ncolorx.todo_app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateTodoRequest {

    private String description;
    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

}
