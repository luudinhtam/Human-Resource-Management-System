package dao.interfaces;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

import entity.Attendance;

public interface IAttendanceDAO {
    void add(Attendance attendance) throws IOException;

    void update(Attendance attendance) throws IOException;
    
    void deleteByEmployeeId(String employeeId) throws IOException;

    List<Attendance> findByEmployeeId(String employeeId) throws IOException;

    Attendance findByEmployeeIdAndDate(String employeeId, LocalDate date) throws IOException;

    List<Attendance> findAll() throws IOException;

    boolean existsByEmployeeIdAndDate(String employeeId, LocalDate date) throws IOException;

    List<Attendance> search(Predicate<Attendance> predicate) throws IOException;

    void saveToFile() throws IOException;
}
