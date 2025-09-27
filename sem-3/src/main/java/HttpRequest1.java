import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

class HttpRequest1 {

    public static void main(String[] args) throws IOException {
        String request = """
                GET / HTTP/1.1
                Accept-Language: ru
                
                """;

        try (
                var socket = new Socket("google.com", 80);
                var out = new PrintWriter(socket.getOutputStream(), true);
                var in = new BufferedInputStream(socket.getInputStream());
                var scanner = new Scanner(in);
        ) {
            out.println(request);

            // TODO
        }
    }
}

