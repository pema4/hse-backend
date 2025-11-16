package todo.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.TestSocketUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringTodoApp.class})
public class SpringTodoAppTest {
    @Autowired
    Server server;

    @DynamicPropertySource
    static void portProperty(DynamicPropertyRegistry propertySource) {
        int availableTcpPort = TestSocketUtils.findAvailableTcpPort();
        propertySource.add("test-port", () -> availableTcpPort);
    }

    @Test
    void handlerTest() throws IOException, InterruptedException {
        var port = ((ServerConnector) server.getConnectors()[0]).getPort();
        try (var httpClient = HttpClient.newHttpClient()) {
            var postRequest = HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/todo/create"))
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
