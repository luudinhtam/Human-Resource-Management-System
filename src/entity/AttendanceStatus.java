package entity;

public enum AttendanceStatus {
    PRESENT, ABSENT, LEAVE;

    public static AttendanceStatus fromString(String s) {
        switch (s.trim().toUpperCase()) {
            case "PRESENT":
                return PRESENT;
            case "ABSENT":
                return ABSENT;
            case "LEAVE":
                return LEAVE;
            default:
                throw new IllegalArgumentException("Invalid status: " + s);
        }
    }
}
