package manager;

import dao.interfaces.ISalaryDAO;
import entity.Employee;
import entity.Salary;
import exception.InactiveEmployeeException;
import util.PrintUtils;

import java.util.List;

public class SalaryManager {

    private static final int REPORT_WIDTH = 95;

    private final ISalaryDAO salaryDAO;

    public SalaryManager(ISalaryDAO salaryDAO) {
        this.salaryDAO = salaryDAO;
    }

    // ── BR10: only active employees ───────────────────────────────────
    public Salary calculateSalary(Employee employee, int month, int year,
            AttendanceManager attendanceManager) throws Exception {
        if (!employee.isActive())
            throw new InactiveEmployeeException(employee.getEmployeeId());

        String id = employee.getEmployeeId();

        // Single-pass — avoids 3 separate traversals of attendance list
        AttendanceManager.AttendanceSummary summary = attendanceManager.getMonthlySummary(id, month, year);

        // BR7, BR8, BR9
        double overtimePay = summary.overtimeHours * employee.getOvertimeRate();
        double absenceDeduction = summary.absentDays * employee.getAbsenceDeductionRate();

        Salary salary = new Salary(id, month, year,
                employee.getBasicSalary(), overtimePay, absenceDeduction,
                summary.workingDays, summary.absentDays, summary.overtimeHours);

        salaryDAO.save(salary);
        System.out.println("[SUCCESS] Salary calculated for " + id
                + " (" + String.format("%02d/%d", month, year) + ")");
        return salary;
    }

    public Salary getSalaryRecord(String employeeId, int month, int year) throws Exception {
        return salaryDAO.findByEmployeeIdAndPeriod(employeeId, month, year);
    }

    public List<Salary> getAllSalariesByMonth(int month, int year) throws Exception {
        return salaryDAO.findByPeriod(month, year);
    }

    public void generateSalaryReport(int month, int year, List<Employee> employees) throws Exception {
        List<Salary> records = salaryDAO.findByPeriod(month, year);

        System.out.println("\n" + PrintUtils.buildDivider('=', REPORT_WIDTH));
        System.out.println(" SALARY REPORT — " + String.format("%02d/%d", month, year));
        System.out.println(PrintUtils.buildDivider('=', REPORT_WIDTH));
        System.out.printf("%-12s %-20s %15s %15s %15s %15s%n",
                "ID", "Name", "Basic (VND)", "Overtime", "Deduction", "Total");
        System.out.println(PrintUtils.buildDivider('-', REPORT_WIDTH));

        if (records.isEmpty()) {
            System.out.println("  No salary records found for this period.");
        } else {
            for (Salary s : records) {
                String name = PrintUtils.findEmployeeName(employees, s.getEmployeeId());
                System.out.printf("%-12s %-20s %,15.0f %,15.0f %,15.0f %,15.0f%n",
                        s.getEmployeeId(), name,
                        s.getBasicSalary(), s.getOvertimePay(),
                        s.getAbsenceDeduction(), s.getTotalSalary());
            }
        }
        System.out.println(PrintUtils.buildDivider('=', REPORT_WIDTH));
    }
}
