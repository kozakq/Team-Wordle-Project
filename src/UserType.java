/*
 * Course: Software Tools & Process
 * Spring 2025
 * Group
 ** @author childressg
 ** @author OrugPeli
 ** @author Griffithjr
 ** @author kozakq
 * @version 1.0
 */
public enum UserType {
    USER,
    ADMIN,
    TESTER,
    COLLEGE_STUDENT,
    MIDDLE_SCHOOL_STUDENT;

    public static UserType typeFromString(String str) {
        return switch (str) {
            case "USER" -> USER;
            case "ADMIN" -> ADMIN;
            case "TESTER" -> TESTER;
            case "COLLEGE_STUDENT" -> COLLEGE_STUDENT;
            case "MIDDLE_SCHOOL_STUDENT" -> MIDDLE_SCHOOL_STUDENT;
            default -> USER;
        };
    }
}