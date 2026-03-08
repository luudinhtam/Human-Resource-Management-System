package entity;

import java.time.LocalDate;

public abstract class Employee {

    private String employeeId;
    private String name;
    private String department;
    private String jobTitle;
    private LocalDate dateOfJoining;
    private double basicSalary;
    private EmployeeStatus status;
    private final EmployeeType type;

    public Employee(String employeeId, String name, String department,
            String jobTitle, LocalDate dateOfJoining,
            double basicSalary, EmployeeType type) {
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.jobTitle = jobTitle;
        this.dateOfJoining = dateOfJoining;
        this.basicSalary = basicSalary;
        this.status = EmployeeStatus.ACTIVE;
        this.type = type;
    }

    // ── Getters ──────────────────────────────────────────────────────
    public String getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public EmployeeStatus getStatus() {
        return status;
    }

    public EmployeeType getType() {
        return type;
    }

    public boolean isActive() {
        return status == EmployeeStatus.ACTIVE;
    }

    // ── Setters ──────────────────────────────────────────────────────
    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setBasicSalary(double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public void setStatus(EmployeeStatus status) {
        this.status = status;
    }

    // ── Abstract methods ─────────────────────────────────────────────
    public abstract double getOvertimeRate();

    public abstract double getAbsenceDeductionRate();

    // ── Display ──────────────────────────────────────────────────────
    public void displayInfo() {
        System.out.println("--------------------------------------------");
        System.out.printf("%-20s: %s%n", "ID", employeeId);
        System.out.printf("%-20s: %s%n", "Name", name);
        System.out.printf("%-20s: %s%n", "Type", type);
        System.out.printf("%-20s: %s%n", "Department", department);
        System.out.printf("%-20s: %s%n", "Job Title", jobTitle);
        System.out.printf("%-20s: %s%n", "Date Joined", dateOfJoining);
        System.out.printf("%-20s: %,.0f VND%n", "Basic Salary", basicSalary);
        System.out.printf("%-20s: %s%n", "Status", status);
    }

    /** Format: id,name,dept,jobTitle,dateJoined,basicSalary,status,type */
    @Override
    public String toString() {
        return employeeId + ","
                + name + ","
                + department + ","
                + jobTitle + ","
                + dateOfJoining + ","
                + basicSalary + ","
                + status + ","
                + type;
    }

}
