package serverclient;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientHandler implements Runnable {

    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {

            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            PrintWriter output = new PrintWriter(
                    socket.getOutputStream(), true);

            String message;

            while ((message = input.readLine()) != null) {

                String time = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

                System.out.println("--------------------------------------");
                System.out.println("Time        : " + time);
                System.out.println("Client IP   : " + socket.getInetAddress().getHostAddress());
                System.out.println("Client Port : " + socket.getPort());
                System.out.println("Message     : " + message);
                System.out.println("--------------------------------------");

                output.println("Server Received : " + message);

                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
            }

            socket.close();
            TCPServer.clientCount.decrementAndGet();

            System.out.println("====================================");
            System.out.println("Client Disconnected");
            System.out.println("Client IP : " + socket.getInetAddress().getHostAddress());
            System.out.println("Active Clients : "
                    + TCPServer.clientCount.get());
            System.out.println("====================================");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}