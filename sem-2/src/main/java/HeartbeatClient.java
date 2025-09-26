import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

class HeartbeatClient {

    public static void main(String[] args) throws IOException {
        try (var socket = new Socket("localhost", 8080)) {
            OutputStream outputStream = socket.getOutputStream();
            var inputStream = socket.getInputStream();

            while (true) {
                var ping = inputStream.readNBytes(4);
                if (!new String(ping).equals("ping")) {
                    throw new RuntimeException();
                }

                var data = inputStream.readNBytes(4);
                System.out.println("Received ping from " + socket);

                outputStream.write("pong".getBytes());
                outputStream.write(data);
                outputStream.flush();
                System.out.println("Sent pong to " + socket);
            }
        }
    }
}

