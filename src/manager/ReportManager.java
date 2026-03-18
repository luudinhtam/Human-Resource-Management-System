package manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import entity.Employee;
import entity.Salary;

public class ReportManager {

    private static final int LOW_ATTENDANCE_THRESHOLD = 3; // BR12

    private final EmployeeManager employeeManager;
    private final AttendanceManager attendanceManager;
    private final SalaryManager salaryManager;

    public ReportManager(EmployeeManager em, AttendanceManager am, SalaryManager sm) {
        this.employeeManager = em;
        this.attendanceManager = am;
        this.salaryManager = sm;
    }

    public static class LowAttendanceEntry { // Inner class to save employee and absentDays
        public final Employee employee;
        public final int absentDays;

        public LowAttendanceEntry(Employee employee, int absentDays) {
            this.employee = employee;
            this.absentDays = absentDays;
        }
    }

    // ── BR12 ──────────────────────────────────────────────────────────
    public List<LowAttendanceEntry> getLowAttendanceEntry(int month, int year) throws Exception {
        List<LowAttendanceEntry> result = new ArrayList<>();

        // Loop for all ACTIVE employee
        for (Employee e : employeeManager.getActiveEmployees()) {
            try {
                    // Get summary for each employee
                AttendanceManager.AttendanceSummary summary = attendanceManager.getMonthlySummary(e.getEmployeeId(), month, year);

                // Only take people who has absentDays > LOW_ATTENDANCE_THRESHOLD
                if (summary.absentDays > LOW_ATTENDANCE_THRESHOLD)
                    result.add(new LowAttendanceEntry(e, summary.absentDays));
            } catch (exception.InvalidInputException ex) {
                //ignore employee with no attendance records (we can not get summary for them)
            }
        }
        return result;
    }

    public void printLowAttendanceReport(int month, int year) throws Exception {
        System.out.println("\n=================================================================");
        System.out.println(" LOW ATTENDANCE on: " + String.format("%02d/%d", month, year)
                + "  (threshold: > " + LOW_ATTENDANCE_THRESHOLD + " absent days)");
        System.out.println("=================================================================");

        List<LowAttendanceEntry> entries = getLowAttendanceEntry(month, year);

        // Check for entries exist
        if (entries.isEmpty()) {
            System.out.println("  No employees with low attendance.");
        } else {
            System.out.printf("%-12s %-20s %-15s %s%n",
                    "ID", "Name", "Department", "Absent Days");
            System.out.println("\n=================================================================");

            for (LowAttendanceEntry entry : entries) {
                System.out.printf("%-12s %-20s %-15s %d%n",
                        entry.employee.getEmployeeId(),
                        entry.employee.getName(),
                        entry.employee.getDepartment(),
                        entry.absentDays);
            }
        }
        System.out.println("\n=================================================================");
    }

    // Comparator
    private static final Comparator<Salary> BY_TOTAL_SALARY_ASC = (a, b) -> Double.compare(a.getTotalSalary(),
            b.getTotalSalary());

    private static final Comparator<Salary> BY_TOTAL_SALARY_DESC = (a, b) -> Double.compare(b.getTotalSalary(),
            a.getTotalSalary());

    // ── BR13 ──────────────────────────────────────────────────────────
    public List<Salary> getHighestPaidEmployees(int month, int year, int topN) throws Exception {
        // Get all salary in this month
        List<Salary> salaries = salaryManager.getAllMonthlySalaries(month, year);

        //
        Collections.sort(salaries, BY_TOTAL_SALARY_DESC);

        // Get N first elements
        return salaries.subList(0, Math.min(topN, salaries.size()));
    }

    public void printHighestPaidReport(int month, int year, int topN) throws Exception {

        if (topN <= 0) {
            throw new IllegalArgumentException("topN must be positive");
        }

        List<Salary> top = getHighestPaidEmployees(month, year, topN);

        System.out.println("\n=================================================================");
        System.out.println(" TOP " + topN + " HIGHEST PAID — "
                + String.format("%02d/%d", month, year));
        System.out.println("=================================================================");

        if (top.isEmpty()) {
            System.out.println("  No salary records found for this period.");
        } else {
            System.out.printf("%-5s %-12s %-20s %15s%n",
                    "Rank", "ID", "Name", "Total (VND)");
            System.out.println("\n=================================================================");
            int rank = 1;
            for (Salary s : top) {
                String name = employeeManager.getEmployeeName(s.getEmployeeId());
                System.out.printf("%-5d %-12s %-20s %,15.0f%n",
                        rank++, s.getEmployeeId(), name, s.getTotalSalary());
            }
        }
        System.out.println("\n=================================================================");
    }
}
