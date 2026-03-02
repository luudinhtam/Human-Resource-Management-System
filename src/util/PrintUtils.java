package util;

import java.util.List;

import entity.Employee;

public class PrintUtils {

    private PrintUtils() {}

    public static String buildDivider(char ch, int width) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < width; i++) sb.append(ch);
        return sb.toString();
    }

    public static void printDivider(char ch, int width) {
        System.out.println(buildDivider(ch, width));
    }

    /** Find employee name from list by ID. Returns "N/A" if not found. */
    public static String findEmployeeName(List<Employee> employees, String employeeId) {
        for (Employee e : employees)
            if (e.getEmployeeId().equals(employeeId)) return e.getName();
        return "N/A";
    }
}
