package org.example;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ExperimentProcessor {
    private final int n;
    private final int m;
    private final int q;
    private final String outputFilePath;

    private InputStream in;
    private OutputStream out;

    public ExperimentProcessor(int arraySizeMultiplier, int numberOfIterations, int numberOfNestedOperations, String outputFilePath){
        n = arraySizeMultiplier;
        m = numberOfIterations;
        q = numberOfNestedOperations;
        this.outputFilePath = outputFilePath;
    }

    public void processExperiment(Socket socket) throws IOException {
        in = socket.getInputStream();
        out = socket.getOutputStream();
        for (long i = 0; i < m; ++i) {
            processIteration(i);
        }
    }


    private void writeResults(long size, long averageDuration){
        try (PrintWriter resultsWriter = new PrintWriter(new FileWriter(outputFilePath, StandardCharsets.UTF_8, true))){
            resultsWriter.println(String.format("%d;%d", size, averageDuration));
        } catch (IOException e) {
            System.err.println("Ошибка на стороне клиента: " + e.getMessage());
        }
    }

    private void processIteration(long iterationNum) throws IOException {
        long curSize = n * iterationNum + 8;
        long curSum = 0;
        for (int j = 0; j < q; ++j){
            String dataForServer = Utils.GenerateRandomString(curSize);
            curSum += sendDataAndMeasureTime(dataForServer);
        }
        long averageTime = curSum / q;
        writeResults(curSize, averageTime);
    }

    private long sendDataAndMeasureTime(String data) throws IOException {
        byte[] dataToSend = data.getBytes();
        byte[] dataLenInBytes = Utils.intToByteArray(dataToSend.length);
        long start = System.currentTimeMillis();
        out.write(dataLenInBytes);
        out.write(dataToSend);
        out.flush();
        // wait for response
        Utils.readBytesFromInputStream(in);
        return System.currentTimeMillis() - start;
    }

}
