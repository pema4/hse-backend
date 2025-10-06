package todo.domain;

import java.time.ZonedDateTime;
import java.util.UUID;

// TodoRepository
// GET /todo/1023
// PUT /todo/1023
// POST /todo/create
// DELETE /todo/1024
// GET /todo

public record Todo(
        long id,
        String text,
        ZonedDateTime creationDate,
        ZonedDateTime completionDate
) {
}
