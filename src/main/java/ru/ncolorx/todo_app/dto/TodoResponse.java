package ru.ncolorx.todo_app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoResponse {
    private Long id;
    private String title;
    private String description;
    private String dueDate;
    private String status;
}
