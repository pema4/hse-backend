package todo.client;

import module java.base;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TodoClient {
    void main(String[] args) {
        try (var httpClient = HttpClient.newHttpClient()) {
            var req = HttpRequest.newBuilder(URI.create("http://localhost:8080/health"))
                    .build();
            var res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            IO.println(res.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}