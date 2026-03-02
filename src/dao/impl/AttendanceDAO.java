package dao.impl;

import dao.interfaces.IAttendanceDAO;
import entity.Attendance;
// import model.AttendanceStatus;
import util.FileManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class AttendanceDAO implements IAttendanceDAO {

    private static final String FILE_PATH = "data/attendance.txt";

    private final ArrayList<Attendance> attendanceList = new ArrayList<Attendance>();
    private final FileManager fileManager;

    public AttendanceDAO() throws Exception {
        this.fileManager = new FileManager(FILE_PATH);
        loadFromFile();
    }

    // ── CRUD ──────────────────────────────────────────────────────────

    @Override
    public void add(Attendance attendance) throws Exception {
        attendanceList.add(attendance);
        saveToFile();
    }

    @Override
    public void update(Attendance updated) throws Exception {
        for (int i = 0; i < attendanceList.size(); i++) {
            Attendance a = attendanceList.get(i);
            if (a.getEmployeeId().equals(updated.getEmployeeId())
                    && a.getDate().equals(updated.getDate())) {
                attendanceList.set(i, updated);
                saveToFile();
                return;
            }
        }
    }

    // ── Queries ───────────────────────────────────────────────────────

    @Override
    public List<Attendance> findByEmployeeId(String employeeId) throws Exception {
        return search(a -> a.getEmployeeId().equals(employeeId));
    }

    @Override
    public Attendance findByEmployeeIdAndDate(String employeeId, LocalDate date) throws Exception {
        for (Attendance a : attendanceList)
            if (a.getEmployeeId().equals(employeeId) && a.getDate().equals(date))
                return a;
        return null;
    }

    @Override
    public List<Attendance> findAll() throws Exception {
        return new ArrayList<Attendance>(attendanceList);
    }

    @Override
    public boolean existsByEmployeeIdAndDate(String employeeId, LocalDate date) throws Exception {
        return findByEmployeeIdAndDate(employeeId, date) != null;
    }

    /**
     * Flexible search using Predicate.
     * Example: search(a -> a.getDate().getMonthValue() == 5 && a.getStatus() ==
     * AttendanceStatus.ABSENT)
     */
    @Override
    public List<Attendance> search(Predicate<Attendance> predicate) throws Exception {
        List<Attendance> result = new ArrayList<Attendance>();
        for (Attendance a : attendanceList)
            if (predicate.test(a))
                result.add(a);
        return result;
    }

    // ── File I/O ──────────────────────────────────────────────────────

    @Override
    public void saveToFile() throws Exception {
        List<String> lines = new ArrayList<String>();
        for (Attendance a : attendanceList)
            lines.add(a.toString());
        fileManager.writeLines(lines);
    }

    private void loadFromFile() throws Exception {
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
