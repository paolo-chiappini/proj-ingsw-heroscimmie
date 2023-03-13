package it.polimi.ingsw;

public class Calculator {

    public int sum(int a, int b) {
        return a + b;
    }

    public int subtraction(int a, int b) {
        return a - b;
    }

    public int multiplication(int a, int b) {
        return a * b;
    }

    public int divison(int a, int b) throws Exception {
        if (b == 0) {
            throw new Exception("Divider can't be zero");
        }

        return a / b;
    }

    public boolean equalIntegers(int a, int b) {
        boolean result = a == b;

        return result;
    }
}
