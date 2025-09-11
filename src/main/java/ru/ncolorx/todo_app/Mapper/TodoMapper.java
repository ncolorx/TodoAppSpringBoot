package ru.ncolorx.todo_app.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.ncolorx.todo_app.DTO.CreateTaskRequest;
import ru.ncolorx.todo_app.DTO.TaskResponse;
import ru.ncolorx.todo_app.DTO.UpdateTaskRequest;
import ru.ncolorx.todo_app.Entity.TodoEntity;

@Mapper(componentModel = "spring")
public interface TodoMapper {
    TaskResponse toResponse(TodoEntity todoEntity);
    TodoEntity fromCreateTaskRequest(CreateTaskRequest taskRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void fromUpdateTaskRequest(UpdateTaskRequest taskRequest, @MappingTarget TodoEntity todoEntity);



}
