package serverclient;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String name;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void sendMessage(String message) {

        if (out != null) {
            out.println(message);
        }
    }

    @Override
    public void run() {

        try {

            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(
                    socket.getOutputStream(), true);

            out.println("Enter your name:");

            name = in.readLine();

            System.out.println("\n====================================");
            System.out.println("Client Connected");
            System.out.println("Name : " + name);
            System.out.println("IP : " + socket.getInetAddress().getHostAddress());
            System.out.println("Port : " + socket.getPort());
            System.out.println("Active Clients : " + TCPServer.clientCount.get());
            System.out.println("====================================");

            TCPServer.broadcast(
                    name + " joined the chat. Active Clients : "
                            + TCPServer.clientCount.get());

            String message;

            while ((message = in.readLine()) != null) {

                if (message.equalsIgnoreCase("exit")) {
                    break;
                }

                String time = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("HH:mm:ss"));

                String fullMessage =
                        "[" + time + "] " + name + " : " + message;

                System.out.println(fullMessage);

                TCPServer.broadcast(fullMessage);
            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            try {

                socket.close();

            } catch (Exception e) {
            }

            TCPServer.clients.remove(this);

            TCPServer.clientCount.decrementAndGet();

            System.out.println(name + " disconnected.");

            TCPServer.broadcast(
                    name + " left the chat. Active Clients : "
                            + TCPServer.clientCount.get());
        }
    }
}