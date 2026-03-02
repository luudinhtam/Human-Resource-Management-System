package manager;

import util.PrintUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import entity.Employee;
import entity.Salary;

public class ReportManager {

    private static final int LOW_ATTENDANCE_THRESHOLD = 3; // BR12
    private static final int REPORT_WIDTH = 65;

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

        for (Employee e : employeeManager.getActiveEmployees()) {

            // int absent = attendanceManager.countAbsentDays(e.getEmployeeId(), month,
            // year);
            AttendanceManager.AttendanceSummary summary = attendanceManager.getMonthlySummary(e.getEmployeeId(), month,
                    year);
            if (summary.absentDays > LOW_ATTENDANCE_THRESHOLD)
                result.add(new LowAttendanceEntry(e, summary.absentDays));
        }
        return result;
    }

    public void printLowAttendanceReport(int month, int year) throws Exception {
        System.out.println("\n" + PrintUtils.buildDivider('=', REPORT_WIDTH));
        System.out.println(" LOW ATTENDANCE — " + String.format("%02d/%d", month, year)
                + "  (threshold: > " + LOW_ATTENDANCE_THRESHOLD + " absent days)");
        System.out.println(PrintUtils.buildDivider('=', REPORT_WIDTH));

        List<LowAttendanceEntry> entries = getLowAttendanceEntry(month, year);

        if (entries.isEmpty()) {
            System.out.println("  No employees with low attendance.");
        } else {
            System.out.printf("%-12s %-20s %-15s %s%n",
                    "ID", "Name", "Department", "Absent Days");
            System.out.println(PrintUtils.buildDivider('-', REPORT_WIDTH));

            for (LowAttendanceEntry entry : entries) {
                System.out.printf("%-12s %-20s %-15s %d%n",
                        entry.employee.getEmployeeId(),
                        entry.employee.getName(),
                        entry.employee.getDepartment(),
                        entry.absentDays);
            }
        }
        System.out.println(PrintUtils.buildDivider('=', REPORT_WIDTH));
    }

    // ── BR13 ──────────────────────────────────────────────────────────
    public List<Salary> getHighestPaidEmployees(int month, int year, int topN) throws Exception {
        List<Salary> salaries = salaryManager.getAllSalariesByMonth(month, year);
        Collections.sort(salaries, new Comparator<Salary>() {
            public int compare(Salary a, Salary b) {
                return Double.compare(b.getTotalSalary(), a.getTotalSalary());
            }
        });
        return salaries.subList(0, Math.min(topN, salaries.size()));
    }

    public void printHighestPaidReport(int month, int year, int topN) throws Exception {
        List<Salary> top = getHighestPaidEmployees(month, year, topN);

        System.out.println("\n" + PrintUtils.buildDivider('=', REPORT_WIDTH));
        System.out.println(" TOP " + topN + " HIGHEST PAID — "
                + String.format("%02d/%d", month, year));
        System.out.println(PrintUtils.buildDivider('=', REPORT_WIDTH));

        if (top.isEmpty()) {
            System.out.println("  No salary records found for this period.");
        } else {
            List<Employee> allEmployees = employeeManager.getAllEmployees();
            System.out.printf("%-5s %-12s %-20s %15s%n",
                    "Rank", "ID", "Name", "Total (VND)");
            System.out.println(PrintUtils.buildDivider('-', REPORT_WIDTH));
            int rank = 1;
            for (Salary s : top) {
                String name = PrintUtils.findEmployeeName(allEmployees, s.getEmployeeId());
                System.out.printf("%-5d %-12s %-20s %,15.0f%n",
                        rank++, s.getEmployeeId(), name, s.getTotalSalary());
            }
        }
        System.out.println(PrintUtils.buildDivider('=', REPORT_WIDTH));
    }
}
