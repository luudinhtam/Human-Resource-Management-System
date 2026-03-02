package exception;

public class EmployeeNotFoundException extends AppException {
    public EmployeeNotFoundException(String id) {
        super("Employee not found with ID: " + id);
    }
}
