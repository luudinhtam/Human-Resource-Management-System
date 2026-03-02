package manager;

import dao.interfaces.IAttendanceDAO;
import entity.Attendance;
import entity.AttendanceStatus;
import exception.AttendanceNotFoundException;
import exception.DuplicateAttendanceException;
import exception.EmployeeNotFoundException;

import java.time.LocalDate;
import java.util.List;
// import java.util.function.Predicate;

public class AttendanceManager {

    private final IAttendanceDAO attendanceDAO;
    private final EmployeeManager employeeManager;

    public AttendanceManager(IAttendanceDAO attendanceDAO, EmployeeManager employeeManager) {
        this.attendanceDAO = attendanceDAO;
        this.employeeManager = employeeManager;
    }

    // ── BR3, BR4 ──────────────────────────────────────────────────────
    public void recordAttendance(Attendance attendance) throws Exception {
        String id = attendance.getEmployeeId();

        if (!employeeManager.exists(id))
            throw new EmployeeNotFoundException(id);

        if (attendanceDAO.existsByEmployeeIdAndDate(id, attendance.getDate()))
            throw new DuplicateAttendanceException(id, attendance.getDate().toString());

        attendanceDAO.add(attendance);
        System.out.println("[SUCCESS] Attendance recorded for " + id
                + " on " + attendance.getDate());
    }

    public void updateAttendance(String employeeId, LocalDate date,
            AttendanceStatus status, double overtimeHours) throws Exception {
        Attendance existing = requireRecord(employeeId, date);
        existing.setStatus(status);
        existing.setOvertimeHours(overtimeHours);
        attendanceDAO.update(existing);
        System.out.println("[SUCCESS] Attendance updated for " + employeeId + " on " + date);
    }

    // ── Queries ───────────────────────────────────────────────────────
    public List<Attendance> getAttendanceByEmployee(String employeeId) throws Exception {
        return attendanceDAO.findByEmployeeId(employeeId);
    }

    public List<Attendance> getAttendanceByMonth(String employeeId, int month, int year) throws Exception {
        return attendanceDAO.search(
                a -> a.getEmployeeId().equals(employeeId)
                        && a.getDate().getMonthValue() == month
                        && a.getDate().getYear() == year);
    }

    /**
     * Single-pass summary — avoids traversing the attendance list 3 times
     * separately
     */
    public AttendanceSummary getMonthlySummary(String employeeId, int month, int year) throws Exception {
        int workingDays = 0;
        int absentDays = 0;
        double overtime = 0;

        for (Attendance a : attendanceDAO.findByEmployeeId(employeeId)) {
            if (a.getDate().getMonthValue() != month || a.getDate().getYear() != year)
                continue;
            switch (a.getStatus()) {
                case PRESENT:
                    workingDays++;
                    overtime += a.getOvertimeHours();
                    break;
                case ABSENT:
                    absentDays++;
                    break;
                default:
                    break;
            }
        }
        return new AttendanceSummary(workingDays, absentDays, (int) overtime);
    }

    // We have to call getMonthlySummary 3 times if we need 3 atributes. We have to
    // fix ^^

    /*
     * ===WARNING===
     * If you only need one number, use the methods below.
     * If you need more than one, call getMonthlySummary() directly.
     */
    public int countWorkingDays(String employeeId, int month, int year) throws Exception {
        return getMonthlySummary(employeeId, month, year).workingDays;
    }

    public int countAbsentDays(String employeeId, int month, int year) throws Exception {
        return getMonthlySummary(employeeId, month, year).absentDays;
    }

    public int getTotalOvertimeHours(String employeeId, int month, int year) throws Exception {
        return getMonthlySummary(employeeId, month, year).overtimeHours;
    }

    // ── Helper ────────────────────────────────────────────────────────
    private Attendance requireRecord(String employeeId, LocalDate date) throws Exception {
        Attendance a = attendanceDAO.findByEmployeeIdAndDate(employeeId, date);
        if (a == null)
            throw new AttendanceNotFoundException(employeeId, date);
        return a;
    }

    // ── Inner class: immutable summary result ─────────────────────────
    public static class AttendanceSummary {
        public final int workingDays;
        public final int absentDays;
        public final int overtimeHours;

        public AttendanceSummary(int workingDays, int absentDays, int overtimeHours) {
            this.workingDays = workingDays;
            this.absentDays = absentDays;
            this.overtimeHours = overtimeHours;
        }
    }
}
