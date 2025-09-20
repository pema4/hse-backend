import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

class Client {

    public static void main(String[] args) throws IOException {
        var datagramSocket = new DatagramSocket();
        //
        datagramSocket.bind(new InetSocketAddress("localhost"));
    }
}

