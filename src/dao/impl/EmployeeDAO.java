package dao.impl;

import dao.interfaces.IEmployeeDAO;
import entity.Employee;
import entity.EmployeeStatus;
import entity.EmployeeType;
import entity.FullTimeEmployee;
import entity.PartTimeEmployee;
import util.FileManager;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public final class EmployeeDAO implements IEmployeeDAO {

    private static final String FILE_PATH = "employee.txt";

    private final ArrayList<Employee> employeeList = new ArrayList<Employee>();
    private final FileManager fileManager;

    public EmployeeDAO() throws IOException {
        this.fileManager = new FileManager(FILE_PATH);
        loadFromFile();
    }

    // ── CRUD ──────────────────────────────────────────────────────────

    @Override
    public void add(Employee employee) throws IOException {
        employeeList.add(employee);
        saveToFile();
    }

    @Override
    public void update(Employee employee) throws IOException {
        for (int i = 0; i < employeeList.size(); i++) {
            if (employeeList.get(i).getEmployeeId().equals(employee.getEmployeeId())) {
                employeeList.set(i, employee);
                saveToFile();
                return;
            }
        }
    }

    @Override
    public void delete(String employeeId) throws IOException {
        for (int i = 0; i < employeeList.size(); i++) {
            if (employeeList.get(i).getEmployeeId().equals(employeeId)) {
                employeeList.remove(i);
                saveToFile();
                return;
            }
        }
    }

    // ── Queries ───────────────────────────────────────────────────────

    @Override
    public Employee findById(String employeeId) throws IOException {
        for (Employee e : employeeList)
            if (e.getEmployeeId().equalsIgnoreCase(employeeId))
                return e;
        return null;
    }

    @Override
    public List<Employee> findAll() throws IOException {
        List<Employee> sorted = new ArrayList<Employee>(employeeList);
        Collections.sort(sorted, new Comparator<Employee>() {
            public int compare(Employee a, Employee b) {
                return a.getEmployeeId().compareTo(b.getEmployeeId());
            }
        });
        return sorted;
    }

    @Override
    public boolean existsById(String employeeId) throws IOException {
        return findById(employeeId) != null;
    }

    /**
     * Flexible search using Predicate — supports any combination of conditions.
     * Example: search(e -> e.getDepartment().equals("IT") && e.isActive())
     */
    @Override
    public List<Employee> search(Predicate<Employee> predicate) throws IOException {
        // Creat a new array list to save the result
        // List is an interface, ArrayList is an implementation of List

        List<Employee> result = new ArrayList<Employee>(); // Creating ArrayList here
        for (Employee e : employeeList)
            // Check every Employee that matches to the Predicate
            if (predicate.test(e))
                result.add(e);
        return result; // Return ArrayList, but we assigned as List

        // Why assigning List instead of ArrayList ? More flexible
        // public ArrayList<Employee> search(...)
        // If we assign ArrayList, in the future, we can change to LinkedList or
        // another, we have to change our code
        // Caller (findByDepartment, findByName...) only know "recieve a List", dont
        // care about what's in it.

    }

    // ── File I/O ──────────────────────────────────────────────────────

    @Override
    public void saveToFile() throws IOException {
        List<String> lines = new ArrayList<String>();
        for (Employee e : employeeList)
            lines.add(e.toString());
        fileManager.writeLines(lines);
    }

    private void loadFromFile() throws IOException {
        employeeList.clear();
        List<String> lines = fileManager.readLines();
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty())
                continue;
            Employee e = parseLine(line);
            if (e != null)
                employeeList.add(e);
        }
    }

    /**
     * Format (FullTime) :
     * id|name|dept|jobTitle|dateJoined|basicSalary|status|FULL_TIME
     * Format (PartTime) :
     * id|name|dept|jobTitle|dateJoined|basicSalary|status|PART_TIME|hoursPerWeek
     */
    private Employee parseLine(String line) {
        try {
            String[] p = line.split(",", -1);
            String id = p[0];
            String name = p[1];
            String dept = p[2];
            String title = p[3];
            LocalDate date = LocalDate.parse(p[4]);
            double salary = Double.parseDouble(p[5]);
            EmployeeStatus status = EmployeeStatus.fromString(p[6]);
            EmployeeType type = EmployeeType.fromString(p[7]);

            Employee emp;
            if (type == EmployeeType.FULL_TIME) {
                emp = new FullTimeEmployee(id, name, dept, title, date, salary);
            } else {
                int hours = p.length > 8 ? Integer.parseInt(p[8].trim()) : 0;
                emp = new PartTimeEmployee(id, name, dept, title, date, salary, hours);
            }
            emp.setStatus(status);
            return emp;
        } catch (Exception e) {
            System.out.println("[WARN] Skipping invalid employee record: " + line);
            return null;
        }
    }

    private static String unescapeField(String value) {
        if (value == null)
            return "";
        return value.replace("\\|", "|").replace("\\\\", "\\");
    }
}
