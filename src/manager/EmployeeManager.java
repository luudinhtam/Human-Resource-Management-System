package manager;

import dao.interfaces.IEmployeeDAO;
import entity.Employee;
import entity.EmployeeStatus;
import exception.DuplicateEmployeeException;
import exception.EmployeeNotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

public class EmployeeManager {

    private final IEmployeeDAO employeeDAO;

    public EmployeeManager(IEmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    public String getEmployeeName(String employeeId) throws IOException {
        Employee e = employeeDAO.findById(employeeId);
        return e != null ? e.getName() : "N/A";
    }

    // ── BR1: ID unique ────────────────────────────────────────────────
    public void addEmployee(Employee employee) throws Exception {
        // Check if the ID exist or not
        if (employeeDAO.existsById(employee.getEmployeeId()))
            throw new DuplicateEmployeeException(employee.getEmployeeId());
        // Add new employee
        employeeDAO.add(employee);
        // Print success
        System.out.println("[SUCCESS] Employee added: " + employee.getEmployeeId());
    }

    public void removeEmployee(String employeeId) throws Exception {
        // Check the employee must exist
        requireExists(employeeId);
        // Delete employee with the ID
        employeeDAO.delete(employeeId);
        // Print success
        System.out.println("[SUCCESS] Employee removed: " + employeeId);
    }

    // BR2: name and department validated in InputValidator before reaching here
    public void updateEmployee(String employeeId, String name, String department,
            String jobTitle, double basicSalary) throws Exception {
        // Check the employee must exist
        Employee emp = requireExists(employeeId);
        // Update employee
        emp.setName(name);
        emp.setDepartment(department);
        emp.setJobTitle(jobTitle);
        emp.setBasicSalary(basicSalary);
        employeeDAO.update(emp);
        // Print success
        System.out.println("[SUCCESS] Employee updated: " + employeeId);
    }

    public void deactivateEmployee(String employeeId) throws Exception {
        // Check the employee must exist
        Employee emp = requireExists(employeeId);
        // Change status to inactive
        emp.setStatus(EmployeeStatus.INACTIVE);
        employeeDAO.update(emp);
        System.out.println("[SUCCESS] Employee deactivated: " + employeeId);
    }

    public void activateEmployee(String employeeId) throws Exception {
        // Check
        Employee emp = requireExists(employeeId);
        // Change status to active
        emp.setStatus(EmployeeStatus.ACTIVE);
        employeeDAO.update(emp);
        System.out.println("[SUCCESS] Employee activated: " + employeeId);
    }

    // ── Queries ───────────────────────────────────────────────────────
    // Public use, other class can call it
    public Employee findById(String employeeId) throws Exception {
        return requireExists(employeeId);
    }

    /** Flexible search — supports any combination of conditions via Predicate */
    public List<Employee> search(Predicate<Employee> predicate) throws Exception {
        return employeeDAO.search(predicate);
        // It returns a List, an interface, not an ArrayList
    }

    public List<Employee> findByName(String name) throws Exception {
        // We pass the Predicate as argument to the method search(),
        return search(e -> e.getName().toLowerCase().contains(name.toLowerCase()));

        // This is a Predicate (Condition) but in shorter way by using Lambda
        // e -> e.getName().toLowerCase().contains(name.toLowerCase())
    }

    public List<Employee> findByDepartment(String department) throws Exception {
        return search(e -> e.getDepartment().equalsIgnoreCase(department));

        // Same meaning but longer, we use lambda for short

        // Predicate<Employee> predicate = e ->
        // e.getDepartment().equalsIgnoreCase(department);
        // return search(predicate);
    }

    public List<Employee> findByJobTitle(String jobTitle) throws Exception {
        return search(e -> e.getJobTitle().equalsIgnoreCase(jobTitle));
    }

    public List<Employee> getAllEmployees() throws Exception {
        return employeeDAO.findAll();
    }

    public List<Employee> getActiveEmployees() throws Exception {
        return search(Employee::isActive);
        // return search(e -> e.isActive());
        // Same meaning but shorter for only calling ONE method
        // But we have to use lambda when it has more complex logic.
    }

    public boolean exists(String employeeId) throws Exception {
        return employeeDAO.existsById(employeeId);
    }

    // ── Helper ────────────────────────────────────────────────────────
    // Private use. only this class can use
    private Employee requireExists(String employeeId) throws Exception {
        Employee e = employeeDAO.findById(employeeId);
        if (e == null)
            throw new EmployeeNotFoundException(employeeId);
        return e;
    }
}
