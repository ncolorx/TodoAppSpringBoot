package ru.ncolorx.todo_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ncolorx.todo_app.enums.Status;
import ru.ncolorx.todo_app.entity.TodoEntity;


import java.util.List;

public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
    List<TodoEntity> findByStatus(Status status);

    List<TodoEntity> findAllByOrderByDueDate();

    List<TodoEntity> findAllByOrderByStatus();

}
