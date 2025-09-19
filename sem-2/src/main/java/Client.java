import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

void main() {
    int port = 8080;
    try (Socket socket = new Socket("localhost", port)) {
        var bytes = socket.getInputStream().readAllBytes();
        var string = new String(bytes, StandardCharsets.UTF_8);
        System.out.println(string);
    } catch (IOException e) {
        System.out.print("Cannot connect to port " + port + ".");
        e.printStackTrace(System.out);
    }
}

