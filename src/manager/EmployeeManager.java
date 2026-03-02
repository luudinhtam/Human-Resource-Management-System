package manager;

import dao.interfaces.IEmployeeDAO;
import entity.Employee;
import entity.EmployeeStatus;
import exception.DuplicateEmployeeException;
import exception.EmployeeNotFoundException;

import java.util.List;
import java.util.function.Predicate;

public class EmployeeManager {

    private final IEmployeeDAO employeeDAO;

    public EmployeeManager(IEmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    // ── BR1: ID unique ────────────────────────────────────────────────
    public void addEmployee(Employee employee) throws Exception {
        if (employeeDAO.existsById(employee.getEmployeeId()))
            throw new DuplicateEmployeeException(employee.getEmployeeId());
        employeeDAO.add(employee);
        System.out.println("[SUCCESS] Employee added: " + employee.getEmployeeId());
    }

    public void removeEmployee(String employeeId) throws Exception {
        requireExists(employeeId);
        employeeDAO.delete(employeeId);
        System.out.println("[SUCCESS] Employee removed: " + employeeId);
    }

    // BR2: name and department validated in InputValidator before reaching here
    public void updateEmployee(String employeeId, String name, String department,
            String jobTitle, double basicSalary) throws Exception {
        Employee emp = requireExists(employeeId);
        emp.setName(name);
        emp.setDepartment(department);
        emp.setJobTitle(jobTitle);
        emp.setBasicSalary(basicSalary);
        employeeDAO.update(emp);
        System.out.println("[SUCCESS] Employee updated: " + employeeId);
    }

    public void deactivateEmployee(String employeeId) throws Exception {
        Employee emp = requireExists(employeeId);
        emp.setStatus(EmployeeStatus.INACTIVE);
        employeeDAO.update(emp);
        System.out.println("[SUCCESS] Employee deactivated: " + employeeId);
    }

    public void activateEmployee(String employeeId) throws Exception {
        Employee emp = requireExists(employeeId);
        emp.setStatus(EmployeeStatus.ACTIVE);
        employeeDAO.update(emp);
        System.out.println("[SUCCESS] Employee activated: " + employeeId);
    }

    // ── Queries ───────────────────────────────────────────────────────
    public Employee findById(String employeeId) throws Exception {
        return requireExists(employeeId);
    }

    /** Flexible search — supports any combination of conditions via Predicate */
    public List<Employee> search(Predicate<Employee> predicate) throws Exception {
        return employeeDAO.search(predicate);
    }

    public List<Employee> findByName(String name) throws Exception {
        return search(e -> e.getName().toLowerCase().contains(name.toLowerCase()));
    }

    public List<Employee> findByDepartment(String department) throws Exception {
        return search(e -> e.getDepartment().equalsIgnoreCase(department));
    }

    public List<Employee> findByJobTitle(String jobTitle) throws Exception {
        return search(e -> e.getJobTitle().equalsIgnoreCase(jobTitle));
    }

    public List<Employee> getAllEmployees() throws Exception {
        return employeeDAO.findAll();
    }

    public List<Employee> getActiveEmployees() throws Exception {
        return search(Employee::isActive);
    }

    public boolean exists(String employeeId) throws Exception {
        return employeeDAO.existsById(employeeId);
    }

    // ── Helper ────────────────────────────────────────────────────────
    private Employee requireExists(String employeeId) throws Exception {
        Employee e = employeeDAO.findById(employeeId);
        if (e == null)
            throw new EmployeeNotFoundException(employeeId);
        return e;
    }
}
