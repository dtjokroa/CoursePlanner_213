package ca.CoursePlanner.Logic;

/**
 * Course Offering class provides 2 static function on calculating the Academic Year
 * and Term Season by passing in the semester code found in the source data.
 */
public class CourseOffering {

    public static int computeYear(int semester) {
        int stripSemester = semester /= 10;
        int firstDigit = stripSemester % 10;
        stripSemester /= 10;
        int secondDigit = stripSemester % 10;
        secondDigit = secondDigit * 10;
        stripSemester /= 10;
        int thirdDigit = stripSemester % 10;
        thirdDigit = thirdDigit * 100;
        return 1900 + thirdDigit + secondDigit + firstDigit;
    }

    public static String computeSeason(int semester) {
        int seasonCode = semester % 10;
        if (seasonCode == 1) {
            return "Spring";
        }
        if (seasonCode == 4) {
            return "Summer";
        }
        if (seasonCode == 7) {
            return "Fall";
        }
        return null;
    }
}
