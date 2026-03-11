package entity;

import java.time.LocalDate;

import util.InputValidator;

public class Attendance {

    private String employeeId;
    private LocalDate date;
    private AttendanceStatus status;
    private double overtimeHours;
    private String note;

    public Attendance(String employeeId, LocalDate date, AttendanceStatus status) {
        this(employeeId, date, status, 0, "");
    }

    public Attendance(String employeeId, LocalDate date, AttendanceStatus status,
            double overtimeHours, String note) {
        this.employeeId = employeeId;
        this.date = date;
        this.status = status;
        this.overtimeHours = overtimeHours;
        this.note = note == null ? "" : note; // ternary operator
        // note is OPTIONAL, we have to check whether user input note or not
        /*
         * Same meaning but longer
         * if (note == null) {
         * this.note = "";
         * } else {
         * this.note = note;
         * }
         */
    }

    // ── Getters ──────────────────────────────────────────────────────
    public String getEmployeeId() {
        return employeeId;
    }

    public LocalDate getDate() {
        return date;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public double getOvertimeHours() {
        return overtimeHours;
    }

    public String getNote() {
        return note;
    }

    // ── Setters ──────────────────────────────────────────────────────
    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

    public void setOvertimeHours(double hours) {
        this.overtimeHours = hours;
    }

    public void setNote(String note) {
        this.note = note == null ? "" : note;
        /*
         * if (note == null) {
         * this.note = "";
         * } else {
         * this.note = note;
         * }
         */
    }

    public void displayInfo() {
        System.out.printf("  %s, %-8s, Overtime: %.1f hrs, Note: %s%n",
                InputValidator.formatDate(date), status, overtimeHours, note);
    }

    // ── Serialization ─────────────────────────────────────────────────
    /** Format: employeeId,date,status,overtimeHours,note */
    // toString is used to FORMAT data before WRITE to a FILE
    @Override
    public String toString() {
        return employeeId + ","
                + date + ","
                + status + ","
                + overtimeHours + ","
                + note;
    }

    // fromString is used to UNFORMAT data from a FILE
    public static Attendance fromString(String line) {
        String[] p = line.split(",", -1);
        /*
         * Example
         * // "EMP001,2024-01-15,PRESENT,2.0,Worked late"
         * String[] p = line.split(",", -1); 
         * // p[0] = "EMP001"
         * // p[1] = "2024-01-15"
         * // p[2] = "PRESENT"
         * // p[3] = "2.0"
         * // p[4] = "Worked late"
         */
        // Create object Attendance with these data
        return new Attendance(
                p[0],
                LocalDate.parse(p[1]),
                AttendanceStatus.fromString(p[2]),
                Double.parseDouble(p[3]),
                p.length > 4 ? p[4] : ""
            );
        // When user input NOTE, p.length > 4 (p.length == 5)
    }
}
