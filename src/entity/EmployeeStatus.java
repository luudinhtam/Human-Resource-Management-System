package entity;
// added LEAVE status (bug: 1.01)
public enum EmployeeStatus {
    ACTIVE, INACTIVE, LEAVE;

    public static EmployeeStatus fromString(String s) {
        switch (s.trim().toUpperCase()) {
            case "ACTIVE":
                return ACTIVE;
            case "INACTIVE":
                return INACTIVE;
            default:
                throw new IllegalArgumentException("Invalid status: " + s);
        }
    }
}
