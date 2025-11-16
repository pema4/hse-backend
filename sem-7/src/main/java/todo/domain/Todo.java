package todo.domain;

import org.jetbrains.annotations.Nullable;

import java.time.ZonedDateTime;

public record Todo(
        @Nullable Long id, // nullable
        String text,
        ZonedDateTime creationDate,
        @Nullable ZonedDateTime completionDate // nullable
) {
}
