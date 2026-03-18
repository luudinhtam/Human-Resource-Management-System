package ui;

import exception.AppException;
import manager.AttendanceManager;
import manager.SalaryManager;
import manager.EmployeeManager;
import entity.Employee;
import entity.FullTimeEmployee;
import entity.PartTimeEmployee;
import util.ConsoleHelper;
import util.InputValidator;

import java.time.LocalDate;
import java.util.List;

public class EmployeeMenuUI {

    private final EmployeeManager employeeManager;
    private final ConsoleHelper console;
    private final AttendanceManager attendanceManager;
    private final SalaryManager salaryManager;
    // Constructor
    public EmployeeMenuUI(EmployeeManager employeeManager, ConsoleHelper console, AttendanceManager attendanceManager, SalaryManager salaryManager) {
        this.employeeManager = employeeManager;
        this.console = console;
        this.attendanceManager = attendanceManager;
        this.salaryManager = salaryManager;
    }

    // Method show()
    public void show() throws Exception {
        boolean back = false;
        while (!back) {
            System.out.println("\n------ EMPLOYEE MANAGEMENT ------");
            System.out.println("  1. Add new employee");
            System.out.println("  2. Update employee");
            System.out.println("  3. Remove employee");
            System.out.println("  4. View all employees");
            System.out.println("  5. Search employee");
            System.out.println("  6. Deactivate / Activate employee");
            System.out.println("  0. Back");
            System.out.println("---------------------------------");
            int choice = console.readInt("Select: ");
            try {
                switch (choice) {
                    case 1:
                        addEmployee();
                        break;
                    case 2:
                        updateEmployee();
                        break;
                    case 3:
                        removeEmployee();
                        break;
                    case 4:
                        viewAllEmployees();
                        break;
                    case 5:
                        searchEmployee();
                        break;
                    case 6:
                        toggleEmployeeStatus();
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

    // Menu for addEmployee()
    private void addEmployee() throws Exception {
        System.out.println("\n--- Add New Employee ---");
        System.out.println("Type: 1=FullTime  2=PartTime");
        int type;
        while (true) {
            type = console.readInt("Choose type: ");
            if (type == 1 || type == 2) break;
            console.printError("Invalid choice. Please enter 1 (FullTime) or 2 (PartTime).");
        }

        String id = console.readString("Employee ID    : ").trim().toUpperCase();
        InputValidator.validateEmployeeId(id);

        String name = console.readString("Name           : ");
        InputValidator.validateName(name);

        String dept = console.readString("Department     : ");
        InputValidator.validateDepartment(dept);

        String title = console.readString("Job Title      : ");
        InputValidator.validateJobTitle(title);

        String dateStr = console.readString("Date of Joining (dd/MM/yyyy): ");
        LocalDate joined = InputValidator.validateDate(dateStr);

        double salary = InputValidator.validatePositiveDouble(
                console.readString("Basic Salary   : "), "Basic Salary");
        InputValidator.validateBasicSalary(salary);

        // We have to validate the information BEFORE calling the class FullTimeEmployee
        // and PartTimeEmployee

        Employee emp;

        if (type == 1) {
            emp = new FullTimeEmployee(id, name, dept, title, joined, salary);

        } else {
            int hours = InputValidator.validatePositiveInt(console.readString("Hours per week : "), "Hours per week");
            InputValidator.validateHoursPerWeek(hours);

            emp = new PartTimeEmployee(id, name, dept, title, joined, salary, hours);
        }

        // When we have the object, we call the class employeeManager with method
        // addEmployee()
        employeeManager.addEmployee(emp);
    }

    private void updateEmployee() throws Exception {
        // We use id to check whether the employee exist or not
        String id = console.readString("Employee ID to update: ").trim().toUpperCase();
        Employee emp = employeeManager.findById(id);

        // If the employee exist, show current info
        System.out.println("Current info:");
        emp.displayInfo();

        // NAME
        String name = console.readString("New name         (Enter to keep): ");
        if (name.trim().isEmpty())
            name = emp.getName();
        InputValidator.validateName(name);

        // DEPARTMENT
        String dept = console.readString("New department   (Enter to keep): ");
        if (dept.trim().isEmpty())
            dept = emp.getDepartment();
        InputValidator.validateDepartment(dept);

        // JOB TITLE
        String title = console.readString("New job title    (Enter to keep): ");
        if (title.trim().isEmpty())
            title = emp.getJobTitle();
        InputValidator.validateJobTitle(title);

        // BASIC SALARY
        String salStr = console.readString("New basic salary (Enter to keep): ");
        double salary = salStr.trim().isEmpty()
                ? emp.getBasicSalary()
                : InputValidator.validatePositiveDouble(salStr, "Basic Salary");
        InputValidator.validateBasicSalary(salary);

        employeeManager.updateEmployee(id, name, dept, title, salary);
    }


    private void removeEmployee() throws Exception {

        // We use ID to find the employee to REMOVE
        String id = console.readString("Employee ID to remove: ").trim().toUpperCase();
        if (console.readConfirm("Are you sure?")) {
            employeeManager.removeEmployee(id);
            attendanceManager.deleteByEmployeeId(id);
            salaryManager.deleteByEmployeeId(id);
        } else {
            System.out.println("Cancelled.");
        }
    }

    private void viewAllEmployees() throws Exception {

        // Call the method getAllEmployees() in employeeManager to view all employees
        List<Employee> list = employeeManager.getAllEmployees();
        if (list.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }
        System.out.println("\n===== ALL EMPLOYEES (" + list.size() + ") =====");
        for (Employee e : list)
            e.displayInfo();
    }

    private void searchEmployee() throws Exception {

        // Choose a way to find out an employee
        System.out.println("Search by: 1=Name  2=Department  3=Job Title  4=ID");
        int choice = console.readInt("Select: ");
        List<Employee> result;
        switch (choice) {
            case 1:
                result = employeeManager.findByName(console.readString("Name: "));
                break;
            case 2:
                result = employeeManager.findByDepartment(console.readString("Department: "));
                break;
            case 3:
                result = employeeManager.findByJobTitle(console.readString("Job Title: "));
                break;
            case 4:
                String id = console.readString("Employee ID: ").trim().toUpperCase();
                Employee emp = employeeManager.findById(id);
                emp.displayInfo();
                return;
            default:
                console.printError("Invalid choice.");
                return;
        }
        if (result.isEmpty())
            System.out.println("No results found.");
        else
            for (Employee e : result)
                e.displayInfo();
    }

    private void toggleEmployeeStatus() throws Exception {
        String id = console.readString("Employee ID: ").trim().toUpperCase();
        Employee emp = employeeManager.findById(id);
        if (emp.isActive())
            employeeManager.deactivateEmployee(id);
        else
            employeeManager.activateEmployee(id);
    }
}
