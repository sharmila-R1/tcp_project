package serverclient;

import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class TCPServer {
	public static AtomicInteger clientCount = new AtomicInteger(0);
    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(5000);

            System.out.println("======================================");
            System.out.println("TCP Server Started...");
            System.out.println("Listening on Port : 5000");
            System.out.println("Waiting for clients...");
            System.out.println("======================================");

            while (true) {

                Socket socket = serverSocket.accept();
                TCPServer.clientCount.incrementAndGet();

                System.out.println("\n======================================");
                System.out.println("New Client Connected");

                System.out.println("Date & Time : "
                        + LocalDateTime.now().format(
                                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));

                System.out.println("Client IP   : "
                        + socket.getInetAddress().getHostAddress());

                System.out.println("Client Port : "
                        + socket.getPort());
                System.out.println("Active Clients : " + TCPServer.clientCount.get());

                System.out.println("======================================");

                ClientHandler handler = new ClientHandler(socket);

                Thread thread = new Thread(handler);

                thread.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}