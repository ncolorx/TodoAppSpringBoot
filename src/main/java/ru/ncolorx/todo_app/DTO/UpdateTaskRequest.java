package ru.ncolorx.todo_app.DTO;

import lombok.Getter;
import lombok.Setter;
import ru.ncolorx.todo_app.Enum.Status;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateTaskRequest {

    private LocalDateTime dueDate;
    private String description;
    private String title;
    private Status status;

}
