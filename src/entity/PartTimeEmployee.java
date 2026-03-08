package entity;

import java.time.LocalDate;

public class PartTimeEmployee extends Employee {

    private static final double OVERTIME_RATE = 50_000;
    private static final double ABSENCE_DEDUCTION = 100_000;

    private int hoursPerWeek;

    public PartTimeEmployee(String employeeId, String name, String department,
            String jobTitle, LocalDate dateOfJoining,
            double basicSalary, int hoursPerWeek) {
        super(employeeId, name, department, jobTitle, dateOfJoining, basicSalary, EmployeeType.PART_TIME);
        this.hoursPerWeek = hoursPerWeek;
    }

    public int getHoursPerWeek() {
        return hoursPerWeek;
    }

    public void setHoursPerWeek(int hoursPerWeek) {
        this.hoursPerWeek = hoursPerWeek;
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
        System.out.printf("%-20s: %d hours%n", "Hours/Week", hoursPerWeek);
        System.out.printf("%-20s: %.0f VND/hour%n", "Overtime Rate", OVERTIME_RATE);
    }

    /** Appends hoursPerWeek to base format */
    @Override
    public String toString() {
        return super.toString() + "," + hoursPerWeek;
    }
}
