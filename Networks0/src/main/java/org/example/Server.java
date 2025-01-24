package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        try {
            System.out.println("Введите порт:");
            int port = getPortFromUser();
            processServerOnPort(port);
        } catch (NumberFormatException ex) {
            System.err.println("Ошибка при распознавании порта");
        } catch (IOException ex) {
            System.err.println("Ошибка на стороне сервера:" + ex.getMessage());
        } finally {
            System.out.println("Сервер завершил работу");
        }
    }

    public static void processServerOnPort(int port) throws IOException {
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
        }
    }
    public static int getPortFromUser() throws NumberFormatException {
        Scanner in = new Scanner(System.in);
        String userInput = in.nextLine();
        return Integer.parseInt(userInput);
    }

}
