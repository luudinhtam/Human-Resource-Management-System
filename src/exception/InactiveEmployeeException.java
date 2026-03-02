package exception;

public class InactiveEmployeeException extends AppException {
    public InactiveEmployeeException(String id) {
        super("Employee is inactive: " + id);
    }
}
