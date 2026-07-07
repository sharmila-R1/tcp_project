package serverclient;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TCPServer {

    public static Set<ClientHandler> clients = ConcurrentHashMap.newKeySet();

    public static AtomicInteger clientCount = new AtomicInteger(0);

    public static void broadcast(String message) {

        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public static void main(String[] args) {

        try {

            ServerSocket serverSocket = new ServerSocket(5000);

            System.out.println("======================================");
            System.out.println("TCP CHAT SERVER STARTED");
            System.out.println("Port : 5000");
            System.out.println("Waiting for clients...");
            System.out.println("======================================");

            while (true) {

                Socket socket = serverSocket.accept();

                ClientHandler handler = new ClientHandler(socket);

                clients.add(handler);

                clientCount.incrementAndGet();

                new Thread(handler).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}