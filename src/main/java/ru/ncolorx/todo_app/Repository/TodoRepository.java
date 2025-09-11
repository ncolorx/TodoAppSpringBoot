package ru.ncolorx.todo_app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ncolorx.todo_app.Enum.Status;
import ru.ncolorx.todo_app.Entity.TodoEntity;


import java.util.List;

public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
    List<TodoEntity> findByStatus(Status status);

    List<TodoEntity> findAllByOrderByDueDate();

    List<TodoEntity> findAllByOrderByStatus();

}
