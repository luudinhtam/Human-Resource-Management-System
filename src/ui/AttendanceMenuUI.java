package ui;

import exception.AppException;
import manager.AttendanceManager;
import entity.Attendance;
import entity.AttendanceStatus;
import util.ConsoleHelper;
import util.InputValidator;

import java.time.LocalDate;
import java.util.List;

public class AttendanceMenuUI {

    private final AttendanceManager attendanceManager;
    private final ConsoleHelper console;

    public AttendanceMenuUI(AttendanceManager attendanceManager, ConsoleHelper console) {
        this.attendanceManager = attendanceManager;
        this.console = console;
    }

    public void show() throws Exception {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== ATTENDANCE MANAGEMENT =====");
            System.out.println("  1. Record attendance");
            System.out.println("  2. Update attendance");
            System.out.println("  3. View attendance history");
            System.out.println("  4. View monthly summary");
            System.out.println("  0. Back");
            System.out.println("===================================");

            int choice = console.readInt("Select: ");
            try {
                switch (choice) {
                    case 1:
                        recordAttendance();
                        break;
                    case 2:
                        updateAttendance();
                        break;
                    case 3:
                        viewAttendanceHistory();
                        break;
                    case 4:
                        viewMonthlySummary();
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

    // ========== ADD ==========
    private void recordAttendance() throws Exception {
        // ID
        String id = console.readString("Employee ID: ");

        // Date
        String dateStr = console.readString("Date (yyyy-MM-dd): ");
        LocalDate date = InputValidator.validateDate(dateStr);

        // Status
        System.out.println("Status: PRESENT / ABSENT / LEAVE");
        AttendanceStatus status = InputValidator.validateAttendanceStatus(console.readString("Status: "));

        // Overtime (If status is PRESENT)
        double overtime = 0;
        if (status == AttendanceStatus.PRESENT) {
            overtime = InputValidator.validatePositiveDouble(console.readString("Overtime hours (0 if none): "),
                    "Overtime hours");
            InputValidator.validateOvertimeHours(overtime);
        }

        String note = console.readString("Note (optional): ");
        // Create a new Attendance Object
        attendanceManager.recordAttendance(new Attendance(id, date, status, overtime, note));
    }

    // ========== UPDATE ==========
    private void updateAttendance() throws Exception {
        // ID
        String id = console.readString("Employee ID: ");

        // Date
        String dateStr = console.readString("Date (yyyy-MM-dd): ");
        LocalDate date = InputValidator.validateDate(dateStr);

        // Status
        System.out.println("New Status: PRESENT / ABSENT / LEAVE");
        AttendanceStatus status = InputValidator.validateAttendanceStatus(console.readString("Status: "));

        // Overtime
        double overtime = 0;
        // Only ask for overtime when status is PRESENT
        if (status == AttendanceStatus.PRESENT) {
            overtime = InputValidator.validatePositiveDouble(console.readString("Overtime hours: "), "Overtime hours");
            InputValidator.validateOvertimeHours(overtime);
        }

        // Update Attendance Object
        attendanceManager.updateAttendance(id, date, status, overtime);
    }

    // // ========== VIEW ==========
    private void viewAttendanceHistory() throws Exception {
        // ID
        String id = console.readString("Employee ID: ");

        // Search a list of employee's attendence records by ID
        List<Attendance> list = attendanceManager.getAttendanceByEmployeeId(id); // getAttendanceByEmployeeId
        if (list.isEmpty()) {
            System.out.println("No records found!. Please try again");
            return;
        }

        System.out.println("\n===== Attendance History with ID: " + id + " =====");
        for (Attendance a : list)
            a.displayInfo();
    }

    private void viewMonthlySummary() throws Exception {
        // ID
        String id = console.readString("Employee ID: ");

        // Month
        int month = console.readInt("Month (1-12): ");

        // Year
        int year = console.readInt("Year: ");

        //Validate
        InputValidator.validateMonth(month);
        InputValidator.validateYear(year);

        //AttendanceSummary to get all info (Working days, absent days, overtime hours)
        AttendanceManager.AttendanceSummary summary = attendanceManager.getMonthlySummary(id, month, year);

        System.out.println("\n===== Monthly Summary: " + id + " " + String.format("%02d/%d", month, year) + " =====");
        // %02d/%d => month = 1, year = 2024 => "01/2024"

        System.out.printf("%-20s: %d%n", "Working days", summary.workingDays);
        System.out.printf("%-20s: %d%n", "Absent days", summary.absentDays);
        System.out.printf("%-20s: %d%n", "Overtime hours", summary.overtimeHours);

        System.out.println("Details:");
        for (Attendance a : attendanceManager.getMonthlyAttendance(id, month, year))
            a.displayInfo();
    }
}
