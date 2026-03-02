package exception;

public class DuplicateAttendanceException extends AppException {
    public DuplicateAttendanceException(String id, String date) {
        super("Attendance already recorded for employee " + id + " on " + date);
    }
}
