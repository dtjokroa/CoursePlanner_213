package ca.CoursePlanner.ApiWrappers;

public class ApiCourseOfferingWrapper {
    public long courseOfferingId;
    public String location;
    public String instructors;
    public String term;
    public long semesterCode;
    public int year;

    @Override
    public String toString() {
        return "ApiCourseOfferingWrapper{" +
                "courseOfferingId=" + courseOfferingId +
                ", location='" + location + '\'' +
                ", instructors='" + instructors + '\'' +
                ", term='" + term + '\'' +
                ", semesterCode=" + semesterCode +
                ", year=" + year +
                '}';
    }
}