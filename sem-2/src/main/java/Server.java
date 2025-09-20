import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.concurrent.Executors;

class Server {

    public static void main(String[] args) throws IOException {
        var executor = Executors.newVirtualThreadPerTaskExecutor();

        try (var serverSocket = new ServerSocket(8080)) {
            while (true) {
                var socket = serverSocket.accept();
                System.out.println("Accepted " + socket);
                executor.submit(() -> {
                    try {
                        Scanner scanner = new Scanner(socket.getInputStream());
                        var string = scanner.nextLine();
                        System.out.println("Received " + string);
                        PrintStream printStream = new PrintStream(socket.getOutputStream());
                        printStream.println("Received " + string);
                        printStream.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }
}