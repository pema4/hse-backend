import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

class HeartbeatServer {

    public static void main(String[] args) {
        int port = switch (args.length) {
            case 0 -> 8080;
            case 1 -> Integer.parseInt(args[0]);
            default -> {
                System.out.println("Usage: java HeartbeatServer.java [port]");
                Runtime.getRuntime().exit(1);
                throw new AssertionError("inaccessible");
            }
        };

        startServerThread(() -> acceptConnections(port));
    }

    private static void startServerThread(Runnable task) {
        Thread serverThread = Thread.ofVirtual().name("server").start(task);
        Thread shutdownHook = Thread.ofVirtual().name("shutdown-hook").unstarted(() -> {
            System.out.println("[server] Exiting, initiating graceful shutdown...");
            serverThread.interrupt();
            try {
                serverThread.join(Duration.ofSeconds(5));
            } catch (InterruptedException e) {
                System.out.println("[server] Forcing shutdown");
            }
        });
        Runtime.getRuntime().addShutdownHook(shutdownHook);

        try {
            serverThread.join();
        } catch (Throwable e) {
            throw new AssertionError("inaccessible");
        }
    }

    private static void acceptConnections(int port) {
        System.out.println("[server] Starting on port " + port);
        try (
                ServerSocket serverSocket = new ServerSocket(8080);
                ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        ) {
            while (!Thread.currentThread().isInterrupted()) {
                Socket socket = serverSocket.accept();
                System.out.println("[" + socket + "] Connection accepted");
                executor.submit(() -> runPingPong(socket));
            }
        } catch (IOException e) {
            System.out.println("[server] Exiting server on port " + port);
        }
    }

    private static void runPingPong(Socket socket) {
        try (
                OutputStream outputStream = socket.getOutputStream();
                InputStream inputStream = socket.getInputStream();
        ) {
            while (!Thread.currentThread().isInterrupted()) {
                byte[] pingData = sendPing(socket, outputStream);

                try (var _ = withTimeout(Duration.ofSeconds(5))) {
                    receivePong(socket, inputStream, pingData);
                } finally {
                    if (Thread.interrupted()) {
                        System.out.println("[" + socket + "] Connection timed out");
                    }
                }

                try {
                    Thread.sleep(Duration.ofSeconds(2));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("[" + socket + "] Connection closed");
        }
    }

    private static void receivePong(Socket socket, InputStream inputStream, byte[] expectedData) throws IOException {
        var pong = inputStream.readNBytes(4);
        if (!new String(pong).equals("pong")) {
            throw new RuntimeException();
        }
        var receivedData = inputStream.readNBytes(4);
        if (!Arrays.equals(expectedData, receivedData)) {
            throw new RuntimeException();
        }
        System.out.println("[" + socket + "] Received pong");
    }

    private static byte[] sendPing(Socket socket, OutputStream outputStream) throws IOException {
        byte[] pingData = new byte[4];
        ThreadLocalRandom.current().nextBytes(pingData);
        outputStream.write("ping".getBytes());
        outputStream.write(pingData);
        outputStream.flush();
        System.out.println("[" + socket + "] Sent ping");
        return pingData;
    }

    private interface Timeout extends AutoCloseable {

        @Override
        void close();
    }

    private static Timeout withTimeout(Duration timeout) {
        var currentThread = Thread.currentThread();
        var cancellerThread = Thread.ofVirtual().start(() -> {
            try {
                Thread.sleep(timeout);
                currentThread.interrupt();
            } catch (InterruptedException _) {
            }
        });

        return cancellerThread::interrupt;
    }
}
