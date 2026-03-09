package manager;

import dao.interfaces.ISalaryDAO;
import entity.Employee;
import entity.Salary;
import exception.InactiveEmployeeException;

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

        // Check for ACTIVE employee
        if (!employee.isActive())
            throw new InactiveEmployeeException(employee.getEmployeeId());

        // ID
        String id = employee.getEmployeeId();

        // AttendanceSumary for all (3) info
        AttendanceManager.AttendanceSummary summary = attendanceManager.getMonthlySummary(id, month, year);

        // BR7, BR8, BR9
        //OVERTIME PAY
        double overtimePay = summary.overtimeHours * employee.getOvertimeRate();

        //ABSENCE DEDUCTION
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
