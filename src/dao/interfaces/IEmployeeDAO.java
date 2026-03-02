package dao.interfaces;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

import entity.Employee;

public interface IEmployeeDAO {
    void add(Employee employee) throws IOException;

    void update(Employee employee) throws IOException;

    void delete(String employeeId) throws IOException;

    Employee findById(String employeeId) throws IOException;

    List<Employee> findAll() throws IOException;

    boolean existsById(String employeeId) throws IOException;

    List<Employee> search(Predicate<Employee> predicate) throws IOException;

    void saveToFile() throws IOException;
}
