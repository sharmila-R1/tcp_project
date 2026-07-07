package serverclient;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {

    public static void main(String[] args) {

        try {

            Socket socket = new Socket("192.168.1.170", 5000);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);

            System.out.println(in.readLine());

            String name = scanner.nextLine();

            out.println(name);

            Thread receiveThread = new Thread(() -> {

                try {

                    String message;

                    while ((message = in.readLine()) != null) {

                        System.out.println(message);
                    }

                } catch (Exception e) {
                }

            });

            receiveThread.start();

            while (true) {

                String message = scanner.nextLine();

                out.println(message);

                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
            }

            socket.close();

            scanner.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}