package todo.client;

import module java.base;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TodoClient {
    void main(String[] args) throws IOException, InterruptedException {
        try (var httpClient = HttpClient.newHttpClient()) {
            var postRequest = HttpRequest.newBuilder(URI.create("http://localhost:8080/todo/create"))
                    .POST(HttpRequest.BodyPublishers.ofString("""
                            {
                              "text": "My todo item"
                            }
                            """))
                    .build();
            var postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
            IO.println("POST Response: " + postResponse);
            IO.println("POST Headers: " + postResponse.headers().map());
            IO.println("POST Body: " + postResponse.body());

            var getRequest = HttpRequest.newBuilder(URI.create("http://localhost:8080/todo/1")).build();
            var getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            IO.println("GET Response: " + getResponse);
            IO.println("GET Headers: " + getResponse.headers());
            IO.println("GET Body: " + getResponse.body());
        }
    }
}