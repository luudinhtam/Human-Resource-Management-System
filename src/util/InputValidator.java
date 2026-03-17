package util;

import exception.InvalidInputException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import entity.AttendanceStatus;

public class InputValidator {

    private InputValidator() {}

    public static void validateEmployeeId(String id) {
        if (id == null || id.trim().isEmpty())
            throw new InvalidInputException("Employee ID must not be empty.");
        if (!id.matches("[A-Za-z0-9_-]+"))
            throw new InvalidInputException("Employee ID can only contain letters, digits, '-' or '_'.");
    }

    public static void validateName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new InvalidInputException("Name must not be empty.");   // BR2
        if (!name.trim().matches("[a-zA-Z\\s]+"))
            throw new InvalidInputException("Name must only contain letters and spaces, no numbers or special characters.");
    }

    public static void validateDepartment(String dept) {
        if (dept == null || dept.trim().isEmpty())
            throw new InvalidInputException("Department must not be empty.");  // BR2
    }

    public static void validateJobTitle(String jobTitle) {
        if (jobTitle == null || jobTitle.trim().isEmpty())
            throw new InvalidInputException("Job title must not be empty.");
        if (!jobTitle.trim().matches("[a-zA-Z\\s]+"))
            throw new InvalidInputException("Job Title must only contain letters and spaces, no numbers or special characters.");
    }

    public static void validateBasicSalary(double salary) {
        if (salary < 0)
            throw new InvalidInputException("Basic salary must be >= 0.");
    }

    public static void validateOvertimeHours(double hours) {
        if (hours < 0)
            throw new InvalidInputException("Overtime hours must be >= 0.");
    }

    public static void validateMonth(int month) {
        if (month < 1 || month > 12)
            throw new InvalidInputException("Month must be between 1 and 12.");
    }

    public static void validateYear(int year) {
        int current = LocalDate.now().getYear();
        if (year < 2000 || year > current)
            throw new InvalidInputException("Year must be between 2000 and " + (current) + ".");
    }

    public static void validateHoursPerWeek(int hours) {
        if (hours <= 0 || hours > 40)
            throw new InvalidInputException("Hours per week must be between 1 and 40.");
    }

    /** BR5 */
    public static AttendanceStatus validateAttendanceStatus(String input) {
        try {
            return AttendanceStatus.fromString(input);
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Status must be PRESENT, ABSENT, or LEAVE.");
        }
    }

    /** Parse date from dd/MM/yyyy */
    public static LocalDate validateDate(String dateStr) {
        try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                return LocalDate.parse(dateStr.trim(), formatter);
            } catch (DateTimeParseException e) {
                throw new InvalidInputException("Date must be in format dd/MM/yyyy (e.g. 15/01/2024).");
            }
        
    }

    public static int validatePositiveInt(String input, String fieldName) {
        try {
            int val = Integer.parseInt(input.trim());
            if (val <= 0) throw new InvalidInputException(fieldName + " must be a positive integer.");
            return val;
        } catch (NumberFormatException e) {
            throw new InvalidInputException(fieldName + " must be a valid integer.");
        }
    }

    public static double validatePositiveDouble(String input, String fieldName) {
        try {
            double val = Double.parseDouble(input.trim());
            if (val < 0) throw new InvalidInputException(fieldName + " must be >= 0.");
            return val;
        } catch (NumberFormatException e) {
            throw new InvalidInputException(fieldName + " must be a valid number.");
        }
    }

    //format Date dd/MM/yyyy
    public static String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
