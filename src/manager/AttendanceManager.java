package manager;

import dao.impl.AttendanceDAO;
import dao.interfaces.IAttendanceDAO;
import entity.Attendance;
import entity.AttendanceStatus;
import exception.AttendanceNotFoundException;
import exception.DuplicateAttendanceException;
import exception.EmployeeNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
// import java.util.function.Predicate;

public class AttendanceManager {

    private final IAttendanceDAO attendanceDAO;
    private final EmployeeManager employeeManager;

    public AttendanceManager(IAttendanceDAO attendanceDAO, EmployeeManager employeeManager) {
        this.attendanceDAO = attendanceDAO;
        this.employeeManager = employeeManager; // Use some methods to check info (ID)
    }

    // BR3, BR4

    // ========== ADD ==========
    public void recordAttendance(Attendance attendance) throws Exception {
        // ID
        String id = attendance.getEmployeeId();

        // Check for ID
        if (!employeeManager.exists(id))
            throw new EmployeeNotFoundException(id);

        // Check exactly date what is
        LocalDate recordDate = attendance.getDate();
        LocalDate currentDate = LocalDate.now();

        // Take info about join date of employee
        entity.Employee employee = employeeManager.findById(id);
        LocalDate joinDate = employee.getDateOfJoining();

        // Check valid date (not future) and (not after current date)
        if (recordDate.isBefore(joinDate) || recordDate.isAfter(currentDate)) {
            throw new exception.InvalidInputException(
                    "Invalid date: " + recordDate + ". Date must be between " + joinDate + " and " + currentDate);
        }
        // Check for ID and Date
        if (attendanceDAO.existsByEmployeeIdAndDate(id, attendance.getDate()))
            throw new DuplicateAttendanceException(id, attendance.getDate().toString());

        // Check INACTIVE and LEAVE employee (bug: 1.01)

        if (employee.getStatus() == entity.EmployeeStatus.INACTIVE
                || employee.getStatus() == entity.EmployeeStatus.LEAVE)
            throw new exception.InactiveEmployeeException("Record can not work for INACTIVE or LEAVE employee: " + id);

        // ADD
        attendanceDAO.add(attendance);
        System.out.println("[SUCCESS] Attendance recorded for " + id
                + " on " + attendance.getDate());
    }

    // ========== UPDATE ==========
    public void updateAttendance(String employeeId, LocalDate date,
            AttendanceStatus status, double overtimeHours) throws Exception {

        // requireRecord(): Check for Id and Date
        Attendance existing = requireRecord(employeeId, date);

        existing.setStatus(status);
        existing.setOvertimeHours(overtimeHours);

        // Update
        attendanceDAO.update(existing);
        System.out.println("[SUCCESS] Attendance updated for " + employeeId + " on " + date);
    }

    // === Queries ===
    // ========== VIEW ==========
    // get Attendance By Employee Id
    public List<Attendance> getAttendanceByEmployeeId(String employeeId) throws Exception {
        return attendanceDAO.findByEmployeeId(employeeId);
    }

    // get all attendance records by Id, Month and Year
    public List<Attendance> getMonthlyAttendance(String employeeId, int month, int year) throws Exception {

        // Search with ID, month and year and return a List
        return attendanceDAO.search(
                a -> a.getEmployeeId().equals(employeeId)
                        && a.getDate().getMonthValue() == month
                        && a.getDate().getYear() == year);
    }

    public AttendanceSummary getMonthlySummary(String employeeId, int month, int year) throws Exception {
        // System.out.println("[DEBUG] getMonthlySummary called");

        // Check employee exists
        if (!employeeManager.exists(employeeId)) {
            throw new EmployeeNotFoundException(employeeId);
        }

        int workingDays = 0;
        int absentDays = 0;
        double overtime = 0;
        int totalRecords = 0;

        List<Attendance> allRecords = attendanceDAO.findByEmployeeId(employeeId);

        for (Attendance a : allRecords) {

            // Check for correct Month and Year
            if (a.getDate().getMonthValue() != month || a.getDate().getYear() != year)
                continue;

            totalRecords++;

            // Only records have correct Month and Year
            switch (a.getStatus()) {
                case PRESENT:
                    workingDays++;
                    overtime += a.getOvertimeHours();
                    break;

                case ABSENT:
                    absentDays++;
                    break;

                case LEAVE:
                    break;

                default:
                    break;
            }
        }

        if (totalRecords == 0) {
            throw new exception.InvalidInputException(
                    "No attendance records found for employee " + employeeId +
                            " in period " + String.format("%02d/%d", month, year) +
                            ". Please record attendance before calculating salary.");
        }

        return new AttendanceSummary(workingDays, absentDays, overtime);
    }

    // public int countWorkingDays(String employeeId, int month, int year) {
    // int count = 0;
    // for (Attendance a : attendanceDAO.findByEmployeeId(employeeId))
    // if (a.getDate().getMonthValue() == month && a.getDate().getYear() == year)
    // if (a.getStatus() == AttendanceStatus.PRESENT)
    // count++;
    // return count;
    // }

    /*
     * === WARNING ===
     * If you only need one number, use the methods below.
     * If you need more than one, call getMonthlySummary() directly.
     */

    public int countWorkingDays(String employeeId, int month, int year) throws Exception {
        return getMonthlySummary(employeeId, month, year).workingDays;
    }

    public int countAbsentDays(String employeeId, int month, int year) throws Exception {
        return getMonthlySummary(employeeId, month, year).absentDays;
    }

    public double getTotalOvertimeHours(String employeeId, int month, int year) throws Exception {
        return getMonthlySummary(employeeId, month, year).overtimeHours;
    }

    // ========== HELPER ==========
    private Attendance requireRecord(String employeeId, LocalDate date) throws Exception {
        Attendance a = attendanceDAO.findByEmployeeIdAndDate(employeeId, date);
        if (a == null)
            throw new AttendanceNotFoundException(employeeId, date);
        return a;
    }

    // Inner class: immutable summary result
    public static class AttendanceSummary {
        public final int workingDays;
        public final int absentDays;
        public final double overtimeHours;

        public AttendanceSummary(int workingDays, int absentDays, double overtimeHours) {
            this.workingDays = workingDays;
            this.absentDays = absentDays;
            this.overtimeHours = overtimeHours;
        }
    }
}
