package ui;

import exception.AppException;
import manager.ReportManager;
import util.ConsoleHelper;
import util.InputValidator;

public class ReportMenuUI {

    private final ReportManager reportManager;
    private final ConsoleHelper console;

    public ReportMenuUI(ReportManager reportManager, ConsoleHelper console) {
        this.reportManager = reportManager;
        this.console = console;
    }

    public void show() throws Exception {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== REPORTS =====");
            System.out.println("  1. Low attendance employees");
            System.out.println("  2. Highest paid employees");
            System.out.println("  0. Back");
            System.out.println("=========================");
            int choice = console.readInt("Select: ");
            try {
                switch (choice) {
                    case 1:
                        reportLowAttendance();
                        break;
                    case 2:
                        reportHighestPaid();
                        break;
                    case 0:
                        back = true;
                        break;
                    default:
                        console.printError("Invalid choice.");
                }
            } catch (AppException e) {
                console.printError(e.getMessage());
            }
        }
    }

    // ── Actions ───────────────────────────────────────────────────────

    private void reportLowAttendance() throws Exception {
        // Input
        int month = console.readInt("Month (1-12): ");
        int year = console.readInt("Year: ");

        // Validate
        InputValidator.validateMonth(month);
        InputValidator.validateYear(year);

        //
        reportManager.printLowAttendanceReport(month, year);
    }

    private void reportHighestPaid() throws Exception {
        // Input
        int month = console.readInt("Month (1-12): ");
        int year = console.readInt("Year: ");
        int topN = console.readInt("Top N: ");

        // Validate
        InputValidator.validateMonth(month);
        InputValidator.validateYear(year);

        //
        reportManager.printHighestPaidReport(month, year, topN);
    }
}
