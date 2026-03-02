package entity;

public enum EmployeeStatus {
    ACTIVE, INACTIVE;

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
