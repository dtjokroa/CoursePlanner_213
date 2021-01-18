package ca.CoursePlanner.Logic;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entry class, the primary class used in the manipulation of the given data,
 * contains each field provided in the source file stored privately and contains
 * the getter functions for each field necessary for encapsulation purposes.
 */

public class Entry {
    private int semester;
    @JsonProperty("subjectName")
    private String subject;
    @JsonProperty("catalogNumber")
    private String catalogueNum;
    private String location;
    @JsonProperty("enrollmentCap")
    private int enrollCapacity;
    @JsonProperty("enrollmentTotal")
    private int enrollTotal;
    private String instructor;
    @JsonProperty("component")
    private String componentCode;

    public Entry(int semester, String subject, String catalogueNum, String location, int enrollCapacity, int enrollTotal, String instructor, String componentCode) {
        this.semester = semester;
        this.subject = subject;
        this.catalogueNum = catalogueNum;
        this.location = location;
        this.enrollCapacity = enrollCapacity;
        this.enrollTotal = enrollTotal;
        this.instructor = instructor;
        this.componentCode = componentCode;
    }

    public Entry() {

    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCatalogueNum() {
        return catalogueNum;
    }

    public void setCatalogueNum(String catalogueNum) {
        this.catalogueNum = catalogueNum;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getEnrollCapacity() {
        return enrollCapacity;
    }

    public void setEnrollCapacity(int enrollCapacity) {
        this.enrollCapacity = enrollCapacity;
    }

    public int getEnrollTotal() {
        return enrollTotal;
    }

    public void setEnrollTotal(int enrollTotal) {
        this.enrollTotal = enrollTotal;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getComponentCode() {
        return componentCode;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "semester=" + semester +
                ", subject='" + subject + '\'' +
                ", catalogueNum='" + catalogueNum + '\'' +
                ", location='" + location + '\'' +
                ", enrollCapacity=" + enrollCapacity +
                ", enrollTotal=" + enrollTotal +
                ", instructor='" + instructor + '\'' +
                ", componentCode='" + componentCode + '\'' +
                '}';
    }

    public boolean equals(Entry obj) {

        return (semester == obj.semester
                && subject.equals(obj.subject)
                && catalogueNum.equals(obj.catalogueNum)
                && location.equals(obj.location)
                && componentCode.equals(obj.componentCode));
    }
}

