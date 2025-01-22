package org.example;

import java.security.InvalidParameterException;

public class UserInput {
    public UserInput(String host, int port, int n, int m, int q){
        this.host = host;
        this.port = port;
        packageSizeMultiplier = n;
        numberOfIterations = m;
        numberOfNestedIterations = q;
    }
    public String host;

    public int port;

    public int packageSizeMultiplier;

    public int numberOfIterations;

    public int numberOfNestedIterations;

    public static UserInput GetUserInputFromString(String input) throws InvalidParameterException, NumberFormatException {
        String[] inputArr = input.split(" ");
        if (inputArr.length != 5)
            throw new InvalidParameterException("Not enough arguments");
        int port = Integer.parseInt(inputArr[1]);
        int n = Integer.parseInt(inputArr[2]);
        int m = Integer.parseInt(inputArr[3]);
        int q = Integer.parseInt(inputArr[4]);
        return new UserInput(inputArr[0], port, n, m, q);
    }
}
