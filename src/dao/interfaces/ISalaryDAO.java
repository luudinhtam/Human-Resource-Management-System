package dao.interfaces;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

import entity.Salary;

public interface ISalaryDAO {
    void save(Salary salary) throws IOException;

    void deleteByPeriod(String employeeId, int month, int year) throws IOException;

    Salary findByEmployeeAndPeriod(String employeeId, int month, int year) throws IOException;

    List<Salary> findByPeriod(int month, int year) throws IOException;

    List<Salary> findAll() throws IOException;

    List<Salary> search(Predicate<Salary> predicate) throws IOException;

    void saveToFile() throws IOException;
}
