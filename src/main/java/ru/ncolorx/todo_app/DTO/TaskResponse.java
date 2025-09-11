package ru.ncolorx.todo_app.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private String dueDate;
    private String status;
}
