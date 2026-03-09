package ui;

import dao.impl.AttendanceDAO;
import dao.impl.EmployeeDAO;
import dao.impl.SalaryDAO;
import manager.AttendanceManager;
import manager.EmployeeManager;
import manager.ReportManager;
import manager.SalaryManager;
import util.ConsoleHelper;

import java.util.Scanner;

public class MenuUI {

    private final EmployeeMenuUI employeeMenu;
    private final AttendanceMenuUI attendanceMenu;
    private final SalaryMenuUI salaryMenu;
    private final ReportMenuUI reportMenu;
    private final ConsoleHelper console;
    Scanner scanner;

    public MenuUI(Scanner scanner) throws Exception {
        this.scanner = scanner;
        this.console = new ConsoleHelper(scanner);

        // Inject DAOs into Managers
        EmployeeManager employeeManager = new EmployeeManager(new EmployeeDAO());
        AttendanceManager attendanceManager = new AttendanceManager(new AttendanceDAO(), employeeManager);
        SalaryManager salaryManager = new SalaryManager(new SalaryDAO(), employeeManager);
        ReportManager reportManager = new ReportManager(employeeManager, attendanceManager, salaryManager);

        this.employeeMenu = new EmployeeMenuUI(employeeManager, console);
        this.attendanceMenu = new AttendanceMenuUI(attendanceManager, console);
        this.salaryMenu = new SalaryMenuUI(salaryManager, employeeManager, attendanceManager, console);
        this.reportMenu = new ReportMenuUI(reportManager, console);
    }

    public void start() {
        System.out.println("|======================================|");
        System.out.println("|   EMPLOYEE MANAGEMENT SYSTEM         |");
        System.out.println("|======================================|");

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = console.readInt("Select: ");
            try {
                switch (choice) {
                    case 1:
                        employeeMenu.show();
                        break;
                    case 2:
                        attendanceMenu.show();
                        break;
                    case 3:
                        salaryMenu.show();
                        break;
                    case 4:
                        reportMenu.show();
                        break;
                    case 0:
                        running = false;
                        break;
                    default:
                        console.printError("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                console.printError("Unexpected error: " + e.getMessage());
            }
        }

        System.out.println("Goodbye!");

    }

    private void printMainMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("  1. Manage Employees");
        System.out.println("  2. Attendance Management");
        System.out.println("  3. Salary Management");
        System.out.println("  4. Reports");
        System.out.println("  0. Exit");
        System.out.println("================================");
    }
}
