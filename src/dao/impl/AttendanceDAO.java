package dao.impl;

import dao.interfaces.IAttendanceDAO;
import entity.Attendance;
import exception.AttendanceNotFoundException;
// import entity.AttendanceStatus;
import util.FileManager;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class AttendanceDAO implements IAttendanceDAO {

    private static final String FILE_PATH = "attendance.txt";

    private final ArrayList<Attendance> attendanceList = new ArrayList<Attendance>();
    private final FileManager fileManager;

    public AttendanceDAO() throws IOException {
        this.fileManager = new FileManager(FILE_PATH);
        loadFromFile();
    }

    // ── CRUD ──────────────────────────────────────────────────────────

    @Override
    public void add(Attendance attendance) throws IOException {
        attendanceList.add(attendance);
        saveToFile();
    }

    @Override
    public void update(Attendance updated) throws IOException {
        boolean found = false;

        //Loop through all the List
        for (int i = 0; i < attendanceList.size(); i++) {
            Attendance a = attendanceList.get(i);
            //Key: (id, date)
            if (a.getEmployeeId().equals(updated.getEmployeeId())
                    && a.getDate().equals(updated.getDate())) {
                //replace an old record by a new record
                attendanceList.set(i, updated); 
                found = true;
                saveToFile();
                return;
            }
        }

        if(!found) {
            throw new AttendanceNotFoundException(updated.getEmployeeId(), updated.getDate());
        }
    }

    // ── Queries ───────────────────────────────────────────────────────

    @Override
    public List<Attendance> findByEmployeeId(String employeeId) throws IOException {
        return search(a -> a.getEmployeeId().equals(employeeId));
    }

    @Override
    // For one specific employee
    public Attendance findByEmployeeIdAndDate(String employeeId, LocalDate date) throws IOException {
        for (Attendance a : attendanceList)
            if (a.getEmployeeId().equals(employeeId) && a.getDate().equals(date))
                return a;
        return null;
        //It is better than search(Predicate)
        //search() is suitable for finding many results
    }

    @Override
    public List<Attendance> findAll() throws IOException {
        return new ArrayList<Attendance>(attendanceList);
    }

    @Override
    public boolean existsByEmployeeIdAndDate(String employeeId, LocalDate date) throws IOException {
        return findByEmployeeIdAndDate(employeeId, date) != null;
    }

    /**
     * Flexible search using Predicate.
     * Example: search(a -> a.getDate().getMonthValue() == 5 && a.getStatus() ==
     * AttendanceStatus.ABSENT)
     */
    @Override
    public List<Attendance> search(Predicate<Attendance> predicate) throws IOException {
        List<Attendance> result = new ArrayList<Attendance>();
        for (Attendance a : attendanceList)
            if (predicate.test(a))
                result.add(a);
        return result;
    }

    // ── File I/O ──────────────────────────────────────────────────────

    @Override
    public void saveToFile() throws IOException {
        List<String> lines = new ArrayList<String>();
        for (Attendance a : attendanceList)
            lines.add(a.toString());
        fileManager.writeLines(lines);
    }

    private void loadFromFile() throws IOException {
        attendanceList.clear();
        List<String> lines = fileManager.readLines();
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty())
                continue;
            try {
                attendanceList.add(Attendance.fromString(line));
            } catch (Exception e) {
                System.out.println("[WARN] Skipping invalid attendance record: " + line);
            }
        }
    }
}
