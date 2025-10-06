package todo.domain;

import java.util.Collection;
import java.util.Optional;

public interface TodoRepository {
    Optional<Todo> find(long id);
    Collection<Todo> findAll(long id);
    void delete(long id);
    void update(Todo todo);
    void create(Todo todo);
}
