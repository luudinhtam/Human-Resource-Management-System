package entity;

import java.time.LocalDate;

public class FullTimeEmployee extends Employee {

    private static final double OVERTIME_RATE = 80_000;
    private static final double ABSENCE_DEDUCTION = 100_000;

    public FullTimeEmployee(String employeeId, String name, String department,
            String jobTitle, LocalDate dateOfJoining, double basicSalary) {
        super(employeeId, name, department, jobTitle, dateOfJoining, basicSalary, EmployeeType.FULL_TIME);
    }

    @Override
    public double getOvertimeRate() {
        return OVERTIME_RATE;
    }

    @Override
    public double getAbsenceDeductionRate() {
        return ABSENCE_DEDUCTION;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.printf("%-20s: %.0f VND/hour%n", "Overtime Rate", OVERTIME_RATE);
    }
}
