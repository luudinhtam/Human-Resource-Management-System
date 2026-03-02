package dao.interfaces;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

import entity.Attendance;

public interface IAttendanceDAO {
    void add(Attendance attendance) throws Exception;

    void update(Attendance attendance) throws Exception;

    List<Attendance> findByEmployeeId(String employeeId) throws Exception;

    Attendance findByEmployeeIdAndDate(String employeeId, LocalDate date) throws Exception;

    List<Attendance> findAll() throws Exception;

    boolean existsByEmployeeIdAndDate(String employeeId, LocalDate date) throws Exception;

    List<Attendance> search(Predicate<Attendance> predicate) throws Exception;

    void saveToFile() throws Exception;
}
