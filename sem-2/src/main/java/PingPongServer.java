import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

class PingPongServer {

    public static ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public static void main(String[] args) throws IOException {
        try (var serverSocket = new ServerSocket(8080)) {
            while (true) {
                var socket = serverSocket.accept();
                System.out.println("Accepted " + socket);
                executor.submit(() -> {
                    runPingPong(socket);
                });
            }
        }
    }

    private static void runPingPong(Socket socket) {
        try {
            while (true) {
                OutputStream outputStream = socket.getOutputStream();
                InputStream inputStream = socket.getInputStream();

                byte[] pingData = sendPing(socket, outputStream);

                Future<?> cancellerTask = startCancellerTask();

                receivePong(socket, inputStream, pingData);

                cancellerTask.cancel(true);
                Thread.sleep(Duration.ofSeconds(2));
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("Exited");
        }
    }

    private static void receivePong(Socket socket, InputStream inputStream, byte[] pingData) throws IOException {
        var pong = inputStream.readNBytes(4);
        if (!new String(pong).equals("pong")) {
            throw new RuntimeException();
        }
        var receivedData = inputStream.readNBytes(4);
        if (!Arrays.equals(pingData, receivedData)) {
            throw new RuntimeException();
        }
        System.out.println("Received pong from " + socket);
    }

    private static Future<?> startCancellerTask() {
        var currentThread = Thread.currentThread();
        var cancellerTask = executor.submit(() -> {
            try {
                Thread.sleep(Duration.ofSeconds(5));
                currentThread.interrupt();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        return cancellerTask;
    }

    private static byte[] sendPing(Socket socket, OutputStream outputStream) throws IOException {
        byte[] pingData = new byte[4];
        ThreadLocalRandom.current().nextBytes(pingData);
        outputStream.write("ping".getBytes());
        outputStream.write(pingData);
        outputStream.flush();
        System.out.println("Sent ping to " + socket);
        return pingData;
    }
}