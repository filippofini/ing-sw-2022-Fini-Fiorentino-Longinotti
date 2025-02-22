package it.polimi.ingsw.CLI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Predicate;

/**
 * This class represents the input parser.
 */
public class InputParser {

    private static final BufferedReader input = new BufferedReader(
            new InputStreamReader(System.in)
    );

    /**
     * This method gets an input line.
     * @return A string.
     */
    public static String getLine() {
        try {
            while (input.ready()) {
                input.readLine(); // Clear any buffered input
            }
            return input.readLine();
        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
            return null;
        }
    }

    /**
     * This method gets a string.
     * @param errorMessage An error message.
     * @param condition A condition.
     * @return A string.
     */
    public static String getString(String errorMessage, Predicate<String> condition) {
        String line;
        do {
            line = getLine();
            if (line != null && condition.test(line)) {
                return line;
            } else {
                System.out.println(errorMessage);
            }
        } while (true);
    }

    /**
     * This method gets an integer.
     * @return An integer.
     */
    public static Integer getInt() {
        while (true) {
            String line = getLine();
            if (line == null) {
                return -1;
            }
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Error, please insert a number");
            }
        }
    }

    /**
     * This method gets an integer.
     * @param errorMessage An error message.
     * @param condition A condition.
     * @return An integer.
     */
    public static Integer getInt(String errorMessage, Predicate<Integer> condition) {
        while (true) {
            Integer i = getInt();
            if (i == null) {
                return -1;
            }
            if (condition.test(i)) {
                return i;
            } else {
                System.out.println(errorMessage);
            }
        }
    }
}