import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ThreadLocalRandom;

class HttpRequest2 {

    public static void main(String[] args) throws IOException, InterruptedException {
        try (HttpClient client = HttpClient.newHttpClient()) {
            while (true) {
                var request = HttpRequest.newBuilder(URI.create("http://localhost:8080/date")).GET().build();
                var response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
                response.thenAccept(resp -> {
                    System.out.println(resp);
                    System.out.println(resp.body());
                });
                Thread.sleep(ThreadLocalRandom.current().nextInt(50, 300));
            }
        }
        // TODO
    }
}
