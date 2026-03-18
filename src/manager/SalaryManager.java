package manager;

import dao.interfaces.ISalaryDAO;
import entity.Attendance;
import entity.Employee;
import entity.Salary;
import exception.InactiveEmployeeException;
import exception.InvalidInputException;

import java.time.LocalDate;
import java.util.List;

public class SalaryManager {

    private final ISalaryDAO salaryDAO;
    private final EmployeeManager employeeManager;

    public SalaryManager(ISalaryDAO salaryDAO, EmployeeManager employeeManager) {
        this.salaryDAO = salaryDAO;
        this.employeeManager = employeeManager;
    }

    // ── BR10: only active employees ───────────────────────────────────
    public Salary calculateSalary(Employee employee, int month, int year,
            AttendanceManager attendanceManager) throws Exception {

        // Check ACTIVE
        if (!employee.isActive())
            throw new InactiveEmployeeException(employee.getEmployeeId());

        // Validate DATE
        // Check salary period must be after or same month as joining date
        LocalDate joinDate = employee.getDateOfJoining();
        LocalDate calcPeriod = LocalDate.of(year, month, 1);

        if (calcPeriod.isBefore(joinDate.withDayOfMonth(1)))
            throw new InvalidInputException(
                    "Salary period " + String.format("%02d/%d", month, year)
                            + " must be after date of joining ("
                            + String.format("%02d/%d", joinDate.getMonthValue(), joinDate.getYear())
                            + ")");

        // ID
        String id = employee.getEmployeeId();

        // Get SUMMARY
        AttendanceManager.AttendanceSummary summary = attendanceManager.getMonthlySummary(id, month, year);

        // Check employee must have >= 1 attendance records
        List<Attendance> monthlyRecords = attendanceManager.getMonthlyAttendance(id, month, year);
        if (monthlyRecords.isEmpty()) {
            throw new exception.InvalidInputException(
                    "No attendance records found for period " +
                            String.format("%02d/%d", month, year) +
                            ". Cannot calculate salary without attendance data.");
        }

        // BR7, BR8, BR9
        double overtimePay = summary.overtimeHours * employee.getOvertimeRate();
        double absenceDeduction = summary.absentDays * employee.getAbsenceDeductionRate();

        Salary salary = new Salary(id, month, year,
                employee.getBasicSalary(), overtimePay, absenceDeduction,
                summary.workingDays, summary.absentDays, summary.overtimeHours);

        salaryDAO.save(salary);
        System.out.println("[SUCCESS] Salary calculated for ID: " + id
                + " (" + String.format("%02d/%d", month, year) + ")");
        return salary;
    }

    public Salary getMonthlySalary(String employeeId, int month, int year) throws Exception {
        return salaryDAO.findByEmployeeAndPeriod(employeeId, month, year);
    }

    //delete salary record of an employee if delete employee
    public void deleteByEmployeeId(String employeeId) throws Exception {
        salaryDAO.deleteByEmployeeId(employeeId);
        System.out.println("[SUCCESS] All salary records deleted: " + employeeId);
    }

    public List<Salary> getAllMonthlySalaries(int month, int year) throws Exception {
        return salaryDAO.findByPeriod(month, year);
    }

    public void generateSalaryReport(int month, int year, List<Employee> employees) throws Exception {
        List<Salary> records = salaryDAO.findByPeriod(month, year);

        System.out.println("\n=================================================================");
        System.out.println(" SALARY REPORT — " + String.format("%02d/%d", month, year));
        System.out.println("\n=================================================================");
        System.out.printf("%-12s %-20s %15s %15s %15s %15s%n",
                "ID", "Name", "Basic (VND)", "Overtime", "Deduction", "Total");
        System.out.println("\n=================================================================");

        if (records.isEmpty()) {
            System.out.println("  No salary records found for this period.");
        } else {
            for (Salary s : records) {
                String name = employeeManager.getEmployeeName(s.getEmployeeId());
                System.out.printf("%-12s %-20s %,15.0f %,15.0f %,15.0f %,15.0f%n",
                        s.getEmployeeId(), name,
                        s.getBasicSalary(), s.getOvertimePay(),
                        s.getAbsenceDeduction(), s.getTotalSalary());
            }
        }

    }
}
