package exception;

public class InvalidInputException extends AppException {
    public InvalidInputException(String message) {
        super("Invalid input: " + message);
    }
}
