import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

class MetricsServer {

    public static void main(String[] args) {
        int port = switch (args.length) {
            case 0 -> 8080;
            case 1 -> Integer.parseInt(args[0]);
            default -> {
                System.out.println("Usage: java HeartbeatServer.java [port]");
                Runtime.getRuntime().exit(1);
                throw new AssertionError("inaccessible");
            }
        };

        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            System.out.print("[server] com.sun.net.httpserver.HttpServer started at port 8080");
            e.printStackTrace(System.out);
            return;
        }

        server.createContext("/now", exchange -> {
            if (exchange.getRequestURI().toASCIIString().startsWith("/now")) {
                String responseTemplate = """
                        {
                          "date": "%s"
                        }""";
                ZonedDateTime date = Instant.now().atZone(ZoneId.of("Europe/Moscow"));
                String responseString = responseTemplate.formatted(date.format(ISO_LOCAL_DATE_TIME));
                byte[] response = UTF_8.encode(responseString).array();

                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.length);
                exchange.getResponseBody().write(response.length);
            }
        });

        server.start();
        System.out.println("[server] HttpServer started at port 8080");

        Thread shutdownHook = Thread.ofVirtual().name("shutdown-hook").unstarted(() -> {
            System.out.println("[server] Exiting, initiating graceful shutdown...");
            server.stop(5);
            System.out.println("[server] HttpServer closed");
        });
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }
}

