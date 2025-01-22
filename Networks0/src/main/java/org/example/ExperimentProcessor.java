package org.example;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ExperimentProcessor {
    private final int n;
    private final int m;
    private final int q;
    private final String outputFilePath;

    private  BufferedReader in;
    private PrintWriter out;

    public ExperimentProcessor(int arraySizeMultiplier, int numberOfIterations, int numberOfNestedOperations, String outputFilePath){
        n = arraySizeMultiplier;
        m = numberOfIterations;
        q = numberOfNestedOperations;
        this.outputFilePath = outputFilePath;
    }

    public void ProcessExperiment(Socket socket) throws IOException {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            this.in = in;
            this.out = out;
            for (long i = 0; i < m; ++i) {
                ProcessIteration(i);
            }
        }
    }


    private void WriteResults(long size, long averageDuration){
        try (PrintWriter resultsWriter = new PrintWriter(new FileWriter(outputFilePath, StandardCharsets.UTF_8, true))){
            resultsWriter.println(String.format("%d;%d", size, averageDuration));
        } catch (IOException e) {
            System.err.println("Ошибка на стороне клиента: " + e.getMessage());
        }
    }

    private void ProcessIteration(long iterationNum) throws IOException {
        long curSize = n * iterationNum + 8;
        long curSum = 0;
        for (int j = 0; j < q; ++j){
            char[] dataForServer = Generator.GenerateRandomString(curSize).toCharArray();
            curSum += SendDataAndMeasureTime(dataForServer);
        }
        long averageTime = curSum / q;
        WriteResults(curSize, averageTime);
    }

    private long SendDataAndMeasureTime(char[] data) throws IOException {
        long start = System.currentTimeMillis();
        out.println(data);
        // wait for response
        in.readLine();
        return System.currentTimeMillis() - start;
    }
}
