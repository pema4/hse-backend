import module java.base;

void main(String[] args) {
    int port = switch (args.length) {
        case 0 -> 8080;
        case 1 -> Integer.parseInt(args[0]);
        default -> {
            IO.println("Usage: java Server.java [port]");
            Runtime.getRuntime().exit(0);
            throw new AssertionError("inaccessible");
        }
    };

    startServerThread(port);
}

private void startServerThread(int port) {
    Thread serverThread = Thread.ofVirtual().name("server").start(() -> acceptConnections(port));
    Thread shutdownHook = Thread.ofVirtual().name("shutdown-hook").unstarted(() -> {
        IO.println("[server] Exiting, initiating graceful shutdown...");
        serverThread.interrupt();
        try {
            serverThread.join(Duration.ofSeconds(5));
        } catch (InterruptedException e) {
            IO.println("[server] Forcing shutdown");
        }
    });
    Runtime.getRuntime().addShutdownHook(shutdownHook);

    try {
        serverThread.join();
    } catch (Throwable e) {
        throw new AssertionError("inaccessible");
    }
}

private void acceptConnections(int port) {
    IO.println("[server] Starting server on port " + port);
}
