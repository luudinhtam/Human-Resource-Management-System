package exception;

public class DuplicateEmployeeException extends AppException {
    public DuplicateEmployeeException(String id) {
        super("Employee already exists with ID: " + id);
    }
}
