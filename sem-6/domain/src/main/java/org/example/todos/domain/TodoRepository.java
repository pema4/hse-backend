package org.example.todos.domain;

import java.util.Collection;
import java.util.Optional;

public interface TodoRepository {
    Optional<Todo> find(long id);

    Collection<Todo> findAll();

    void delete(long id);

    void update(Todo todo);

    Todo create(Todo todo);
}
