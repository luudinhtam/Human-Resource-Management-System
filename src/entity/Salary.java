package entity;

public class Salary {

    private String employeeId;
    private int month;
    private int year;
    private double basicSalary;
    private double overtimePay;
    private double absenceDeduction;
    private double totalSalary;
    private int workingDays;
    private int absentDays;
    private double overtimeHours;

    public Salary(String employeeId, int month, int year,
            double basicSalary, double overtimePay, double absenceDeduction,
            int workingDays, int absentDays, double overtimeHours) {
        this.employeeId = employeeId;
        this.month = month;
        this.year = year;
        this.basicSalary = basicSalary;
        this.overtimePay = overtimePay;
        this.absenceDeduction = absenceDeduction;
        this.totalSalary = basicSalary + overtimePay - absenceDeduction;
        this.workingDays = workingDays;
        this.absentDays = absentDays;
        this.overtimeHours = overtimeHours;
    }

    // ── Getters ──────────────────────────────────────────────────────
    public String getEmployeeId() {
        return employeeId;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public double getOvertimePay() {
        return overtimePay;
    }

    public double getAbsenceDeduction() {
        return absenceDeduction;
    }

    public double getTotalSalary() {
        return totalSalary;
    }

    public int getWorkingDays() {
        return workingDays;
    }

    public int getAbsentDays() {
        return absentDays;
    }

    public double getOvertimeHours() {
        return overtimeHours;
    }

    public void displaySalaryDetail() {
        System.out.println("--------------------------------------------");
        System.out.printf("Employee ID      : %s%n", employeeId);
        System.out.printf("Period           : %02d/%d%n", month, year);
        System.out.printf("Basic Salary     : %,.0f VND%n", basicSalary);
        System.out.printf("Overtime Pay     : %,.0f VND  (%f hrs)%n", overtimePay, overtimeHours);
        System.out.printf("Absence Deduction: %,.0f VND  (%d days)%n", absenceDeduction, absentDays);
        System.out.printf("Working Days     : %d%n", workingDays);
        System.out.println("--------------------------------------------");
        System.out.printf("TOTAL SALARY     : %,.0f VND%n", totalSalary);
        System.out.println("--------------------------------------------");
    }

    /**
     * Format:
     * employeeId,month,year,basicSalary,overtimePay,absenceDeduction,workingDays,absentDays,overtimeHours
     */
    @Override
    public String toString() {
        return employeeId + "," + month + "," + year + ","
                + basicSalary + "," + overtimePay + "," + absenceDeduction + ","
                + workingDays + "," + absentDays + "," + overtimeHours;
    }

    public static Salary fromString(String line) {
        String[] p = line.split(",");
        return new Salary(
                p[0],
                Integer.parseInt(p[1]),
                Integer.parseInt(p[2]),
                Double.parseDouble(p[3]),
                Double.parseDouble(p[4]),
                Double.parseDouble(p[5]),
                Integer.parseInt(p[6]),
                Integer.parseInt(p[7]),
                Double.parseDouble(p[8]));
    }
}
