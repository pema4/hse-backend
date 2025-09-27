import org.intellij.lang.annotations.Language;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

class DummyHttpServer {

    @Language("HTTP")
    private static final String req = """
    GET google.com
    """;

    public static void main(String[] args) throws IOException {
        try (var socket = new Socket("localhost", 8080)) {
            @Language("HTTP")
            var request = "GET google.com";

            var printer = new PrintStream(socket.getOutputStream());
            printer.println("Hello, server");
            printer.flush();

            var string = new Scanner(socket.getInputStream()).nextLine();
            System.out.println("Received " + string);
        }
    }
}

