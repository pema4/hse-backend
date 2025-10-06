package todo.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.http.HttpURI;
import org.eclipse.jetty.io.Content;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.util.Callback;
import todo.domain.Todo;
import todo.domain.TodoRepository;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.regex.Pattern;

public class TodoHandler extends Handler.Abstract {
    private final TodoRepository todoRepository;
    private static final Pattern URI_PATTERN = Pattern.compile("/todo(/[1-9]\\d*|/create)?/?");
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public TodoHandler(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public boolean handle(Request request, Response response, Callback callback) throws IOException {
        HttpMethod httpMethod = HttpMethod.fromString(request.getMethod());
        HttpURI uri = request.getHttpURI();
        var match = URI_PATTERN.matcher(uri.getPath());
        if (!match.matches()) {
            return false;
        }

        String pathVariable;
        try {
            pathVariable = match.group(1).substring(1);
        } catch (IndexOutOfBoundsException _) {
            pathVariable = null;
        }
        Long id = toLongOrNull(pathVariable);

        String body = Content.Source.asString(request);

        if (pathVariable == null) {
            if (httpMethod == HttpMethod.GET) {
                doGetAll(response, callback);
                return true;
            }
        } else {
            switch (httpMethod) {
                case HttpMethod.GET -> {
                    if (id != null) {
                        doGet(id, response, callback);
                        return true;
                    }
                }
                case PUT -> {
                    if (id != null) {
                        doPut(id, body, response, callback);
                        return true;
                    }
                }
                case POST -> {
                    if (pathVariable.equals("create")) {
                        doPost(body, response, callback);
                        return true;
                    }
                }
                case DELETE -> {
                    if (id != null) {
                        doDelete(id, response, callback);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private Long toLongOrNull(String x) {
        try {
            return Long.parseLong(x);
        } catch (NumberFormatException _) {
            return null;
        }
    }

    private void doGet(long id, Response response, Callback callback) {
        var todo = todoRepository.find(id).orElse(null);
        var json = toJson(todo);
        ByteBuffer byteBuffer = BufferUtil.toBuffer(json, StandardCharsets.UTF_8);
        response.setStatus(HttpStatus.OK_200);
        response.write(true, byteBuffer, callback);
    }

    private static String toJson(Object todo) {
        try {
            return MAPPER.writeValueAsString(todo);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void doGetAll(Response response, Callback callback) {

    }

    private void doPut(long id, String body, Response response, Callback callback) {

    }

    private void doPost(String request, Response response, Callback callback) {
        Todo todo;
        try {
            todo = MAPPER.readValue(request, Todo.class);
        } catch (JsonProcessingException e) {
            callback.failed(e);
            return;
        }

        var inserted = todoRepository.create(todo);
        var json = toJson(inserted);
        ByteBuffer byteBuffer = BufferUtil.toBuffer(json, StandardCharsets.UTF_8);
        response.setStatus(HttpStatus.OK_200);
        response.write(true, byteBuffer, callback);
    }

    private void doDelete(long id, Response response, Callback callback) {

    }
}
