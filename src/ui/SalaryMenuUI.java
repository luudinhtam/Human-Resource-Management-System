package ui;

import exception.AppException;
import manager.AttendanceManager;
import manager.EmployeeManager;
import manager.SalaryManager;
import entity.Employee;
import entity.Salary;
import util.ConsoleHelper;
import util.InputValidator;

public class SalaryMenuUI {

    private final SalaryManager salaryManager;
    private final EmployeeManager employeeManager;
    private final AttendanceManager attendanceManager;
    private final ConsoleHelper console;

    public SalaryMenuUI(SalaryManager salaryManager,
            EmployeeManager employeeManager,
            AttendanceManager attendanceManager,
            ConsoleHelper console) {
        this.salaryManager = salaryManager;
        this.employeeManager = employeeManager;
        this.attendanceManager = attendanceManager;
        this.console = console;
    }

    public void show() throws Exception {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== SALARY MANAGEMENT =====");
            System.out.println("  1. Calculate salary for employee");
            System.out.println("  2. View salary detail");
            System.out.println("  3. Generate salary report (all)");
            System.out.println("  0. Back");
            System.out.println("==============================");
            int choice = console.readInt("Select: ");
            try {
                switch (choice) {
                    case 1:
                        calculateSalary();
                        break;
                    case 2:
                        viewSalaryDetail();
                        break;
                    case 3:
                        generateSalaryReport();
                        break;
                    case 0:
                        back = true;
                        break;
                    default:
                        console.printError("Invalid choice! Please try again");
                }
            } catch (AppException e) {
                console.printError(e.getMessage());
            }
        }
    }

    // ── Actions ───────────────────────────────────────────────────────

    private void calculateSalary() throws Exception {
        //Input
        String id = console.readString("Employee ID: ").trim().toUpperCase();
        int month = console.readInt("Month (1-12): ");
        int year = console.readInt("Year: ");

        //Validator
        InputValidator.validateMonth(month);
        InputValidator.validateYear(year);

        // Require employee exists
        Employee emp = employeeManager.findById(id);

        //Calculate salary
        Salary salary = salaryManager.calculateSalary(emp, month, year, attendanceManager);
        salary.displaySalaryDetail();
    }

    private void viewSalaryDetail() throws Exception {

        //Input
        String id = console.readString("Employee ID: ").trim().toUpperCase();
        int month = console.readInt("Month (1-12): ");
        int year = console.readInt("Year: ");

        //Validator
        InputValidator.validateMonth(month);
        InputValidator.validateYear(year);

        //Require salary record exists
        Salary s = salaryManager.getMonthlySalary(id, month, year);
        if (s == null)
            console.printError("No salary record found. Please calculate first.");
        else
            s.displaySalaryDetail();
    }

    private void generateSalaryReport() throws Exception {

        //Input
        int month = console.readInt("Month (1-12): ");
        int year = console.readInt("Year: ");

        //Validator
        InputValidator.validateMonth(month);
        InputValidator.validateYear(year);

        //Report
        salaryManager.generateSalaryReport(month, year, employeeManager.getAllEmployees());
    }
}
