package ru.ncolorx.todo_app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.ncolorx.todo_app.dto.CreateTodoRequest;
import ru.ncolorx.todo_app.dto.TodoResponse;
import ru.ncolorx.todo_app.dto.UpdateTodoRequest;
import ru.ncolorx.todo_app.entity.TodoEntity;

@Mapper(componentModel = "spring")
public interface TodoMapper {
    TodoResponse toResponse(TodoEntity todoEntity);

    TodoEntity fromCreateTaskRequest(CreateTodoRequest taskRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void fromUpdateTaskRequest(UpdateTodoRequest taskRequest, @MappingTarget TodoEntity todoEntity);
}
