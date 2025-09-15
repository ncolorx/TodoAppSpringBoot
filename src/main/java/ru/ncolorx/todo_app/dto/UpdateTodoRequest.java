package ru.ncolorx.todo_app.dto;

import lombok.Getter;
import lombok.Setter;
import ru.ncolorx.todo_app.enums.Status;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateTodoRequest {

    private LocalDateTime dueDate;
    private String description;
    private String title;
    private Status status;

}
