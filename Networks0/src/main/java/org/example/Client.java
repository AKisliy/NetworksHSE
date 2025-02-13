package org.example;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner consoleIn = new Scanner(System.in);
        String userInputStr = consoleIn.nextLine();

        UserInput input = UserInput.GetUserInputFromString(userInputStr);
        ExperimentProcessor processor = new ExperimentProcessor(
                input.packageSizeMultiplier,
                input.numberOfIterations,
                input.numberOfNestedIterations,
                "results.csv"
        );

        try (Socket socket = new Socket(input.host, input.port)) {
            System.out.println("Подключен к серверу! (v3)");
            socket.setTcpNoDelay(true);
            processor.processExperiment(socket);
        } catch (IOException e) {
            System.err.println("Ошибка на стороне клиента: " + e.getMessage());
        }
    }
}

