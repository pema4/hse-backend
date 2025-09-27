import org.intellij.lang.annotations.Language;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class RawHttpRequestExample {

    public static void main(String[] args) throws IOException {
        try (
                var socket = new Socket("www.google.com", 80);
                var out = new PrintWriter(socket.getOutputStream(), true);
                var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            @Language("HTTP")
            String request = """
                    GET / HTTP/1.1
                    Accept-Language: ru
                    Host: www.google.com
                    
                    """;
            out.println(request);

            String response = in.readLine();
            List<String> headers = new ArrayList<>();
            String line = in.readLine();
            while (!line.isEmpty()) {
                headers.add(line);
                line = in.readLine();
            }
            System.out.println(response);
            System.out.println(headers);
        }
    }
}

