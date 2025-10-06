import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

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

        AtomicInteger inflightRequests = new AtomicInteger();
        BlockingQueue<Integer> queue = new SynchronousQueue<>();

        server.createContext("/date", exchange -> {
            try (exchange) {
                exchange.getRequestBody().readAllBytes();
                LocalDateTime response = LocalDateTime.now();
                var responseString = response.atZone(ZoneId.of("Europe/Moscow")).format(DateTimeFormatter.ISO_DATE_TIME);
                var responseBytes = responseString.getBytes(StandardCharsets.UTF_8);

                var current = inflightRequests.incrementAndGet();
                queue.offer(current);
                Thread.sleep(ThreadLocalRandom.current().nextInt(100, 1000));

                exchange.sendResponseHeaders(200, responseBytes.length);
                exchange.getResponseBody().write(responseBytes);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                var current = inflightRequests.decrementAndGet();
                queue.offer(current);
            }
        });

        server.start();
        System.out.println("[server] HttpServer started at port 8080");

        try (var serverSocket = new ServerSocket(8081)) {
            while (true) {
                try (
                        var socket = serverSocket.accept();
                        var out = new PrintWriter(socket.getOutputStream(), true);
                ) {
                    while (true) {
                        var c = queue.take();
                        out.println(c);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

// polling
// 2. short polling
// 3. long polling
// 5. SSE
// 4. keep alive
// 6. websocket
