package ru.ncolorx.todo_app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.ncolorx.todo_app.Entity.TodoEntity;
import ru.ncolorx.todo_app.Enum.Status;
import ru.ncolorx.todo_app.Repository.TodoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    void testSaveAndFindByStatus() {
        TodoEntity todo = new TodoEntity();
        todo.setTitle("Repo Test");
        todo.setDescription("Check repository");
        todo.setStatus(Status.TODO);
        todo.setCreatedAt(LocalDateTime.now());
        todo.setDueDate(LocalDate.now().plusDays(1));

        todoRepository.save(todo);

        List<TodoEntity> todos = todoRepository.findByStatus(Status.TODO);
        assertThat(todos).isNotEmpty();
        assertThat(todos.get(0).getTitle()).isEqualTo("Repo Test");
    }

    @Test
    void testFindAllByOrderByDueDate() {
        TodoEntity t1 = new TodoEntity();
        t1.setTitle("Task1");
        t1.setDescription("desc");
        t1.setStatus(Status.TODO);
        t1.setCreatedAt(LocalDateTime.now());
        t1.setDueDate(LocalDate.now().plusDays(5));

        TodoEntity t2 = new TodoEntity();
        t2.setTitle("Task2");
        t2.setDescription("desc");
        t2.setStatus(Status.TODO);
        t2.setCreatedAt(LocalDateTime.now());
        t2.setDueDate(LocalDate.now().plusDays(1));

        todoRepository.saveAll(List.of(t1, t2));

        List<TodoEntity> todos = todoRepository.findAllByOrderByDueDate();
        assertThat(todos.get(0).getTitle()).isEqualTo("Task2");
    }
}
