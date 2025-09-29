import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

class HttpServerExample {

    public static void main(String[] args) {
        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress(8080), 0);
        } catch (IOException e) {
            System.out.print("[server] Cannot start server. ");
            e.printStackTrace(System.out);
            return;
        }

        server.createContext("/date", exchange -> {
            try (exchange) {
                exchange.getRequestBody().readAllBytes();
                LocalDateTime response = LocalDateTime.now();
                var responseString = response.atZone(ZoneId.of("Europe/Moscow")).format(DateTimeFormatter.ISO_DATE);
                var responseBytes = responseString.getBytes(StandardCharsets.UTF_8);

                Thread.sleep(ThreadLocalRandom.current().nextInt(100, 1000));

                exchange.sendResponseHeaders(200, responseBytes.length);
                exchange.getResponseBody().write(responseBytes);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        server.createContext("/inflight", exchange -> {
            try (exchange) {
                // TODO()
            }
        });

        server.start();
        System.out.println("[server] HttpServer started at port 8080");
    }
}

// polling
// 2. short polling
// 3. long polling
// 5. SSE
// 4. keep alive
// 6. websocket
