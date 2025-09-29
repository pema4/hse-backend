import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

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

            var status = scanner.nextLine();
            System.out.println(status);
            var headers = new ArrayList<String>();
            var line = scanner.nextLine();
            while (!line.isEmpty()) {
                headers.add(line);
                line = scanner.nextLine();
            }
            System.out.println(headers.stream().collect(Collectors.joining("\n")));
        }
    }
}

