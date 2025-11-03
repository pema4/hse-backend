package org.example.todos.infrastructure;

import org.example.todos.domain.Todo;
import org.example.todos.domain.TodoRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class TodoRepositoryImpl implements TodoRepository {
    private final Map<Long, Todo> storage = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong();

    @Override
    public Optional<Todo> find(long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Collection<Todo> findAll() {
        return new HashSet<>(storage.values());
    }

    @Override
    public void delete(long id) {
        storage.remove(id);
    }

    @Override
    public void update(Todo todo) {
        storage.put(todo.id(), todo);
    }

    @Override
    public Todo create(Todo todo) {
        var newId = idCounter.incrementAndGet();
        var insertedTodo = new Todo(newId, todo.text(), todo.creationDate(), todo.completionDate());
        storage.put(newId, insertedTodo);
        return insertedTodo;
    }
}
