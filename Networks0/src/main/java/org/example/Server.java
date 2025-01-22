package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {
    public static void main(String[] args) {
        int port = 25252;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен, ожидаем подключения...");

            try (Socket clientSocket = serverSocket.accept()) {
                System.out.println("Клиент подключен!");

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                while (in.readLine() != null) {
                    String currentTime = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date());
                    out.println(currentTime);
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка на стороне сервера: " + e.getMessage());
        }
    }

}
