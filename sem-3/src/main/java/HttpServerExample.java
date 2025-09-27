import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

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

                // TODO
            }
        });

        server.start();
        System.out.println("[server] HttpServer started at port 8080");
    }
}

