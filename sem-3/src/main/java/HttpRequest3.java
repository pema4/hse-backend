import java.io.IOException;
import java.net.Socket;
import java.time.Instant;
import java.util.Scanner;

class HttpRequest3 {

    public static void main(String[] args) throws IOException, InterruptedException {
        try (
                var socket = new Socket("localhost", 8081);
                var scanner = new Scanner(socket.getInputStream());
        ) {
            while (scanner.hasNextLine()) {
                System.out.println(Instant.now());
                System.out.println(scanner.nextLine());
            }
        }
    }
}
