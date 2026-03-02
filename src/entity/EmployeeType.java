package entity;

public enum EmployeeType {
    FULL_TIME, PART_TIME;

    public static EmployeeType fromString(String s) {
        switch (s.trim().toUpperCase()) {
            case "FULL_TIME":
                return FULL_TIME;
            case "PART_TIME":
                return PART_TIME;
            default:
                throw new IllegalArgumentException("Invalid type: " + s);
        }
    }
}
