import java.util.Scanner;

import ui.MenuUI;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            new MenuUI(scanner).start();
        } catch (Exception e) {
            System.out.println("[FATAL] Failed to start: " + e.getMessage());
        }
    }
}
