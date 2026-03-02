package util;

import java.util.Scanner;

public class ConsoleHelper {

    private static final String SUCCESS_PREFIX = "[SUCCESS] ";
    private static final String ERROR_PREFIX = "[ERROR] ";

    private Scanner scanner;

    public ConsoleHelper(Scanner scanner) {
        this.scanner = scanner;
    }

    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                printError("Please enter a valid integer.");
            }
        }
    }

    public double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                printError("Please enter a valid number.");
            }
        }
    }

    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public boolean readConfirm(String prompt) {
        System.out.print(prompt + " (y/n): ");
        return scanner.nextLine().trim().equalsIgnoreCase("y");
    }

    public void printSuccess(String message) {
        System.out.println(SUCCESS_PREFIX + message);
    }

    public void printError(String message) {
        System.out.println(ERROR_PREFIX + message);
    }

    public void printLine(String label, String value) {
        System.out.printf("%-20s: %s%n", label, value);
    }
}
