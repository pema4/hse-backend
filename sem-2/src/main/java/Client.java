import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

class Client {

    public static void main(String[] args) throws IOException {
        try (var socket = new Socket("localhost", 8080)) {
            var printer = new PrintStream(socket.getOutputStream());
            printer.println("Hello, server");
            printer.flush();

            var string = new Scanner(socket.getInputStream()).nextLine();
            System.out.println("Received " + string);
        }
    }
}

