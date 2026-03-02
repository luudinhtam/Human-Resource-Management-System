package dao.interfaces;

import java.util.List;
import java.util.function.Predicate;

import entity.Salary;

public interface ISalaryDAO {
    void save(Salary salary) throws Exception;

    void deleteByEmployeeIdAndPeriod(String employeeId, int month, int year) throws Exception;

    Salary findByEmployeeIdAndPeriod(String employeeId, int month, int year) throws Exception;

    List<Salary> findByPeriod(int month, int year) throws Exception;

    List<Salary> findAll() throws Exception;

    List<Salary> search(Predicate<Salary> predicate) throws Exception;

    void saveToFile() throws Exception;
}
