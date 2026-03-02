package exception;

import java.time.LocalDate;

public class AttendanceNotFoundException extends AppException {
    public AttendanceNotFoundException(String employeeId, LocalDate date) {
        super("No attendance record found for employee " + employeeId + " on " + date);
    }
}
