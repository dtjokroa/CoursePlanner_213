package ca.CoursePlanner.Controller;

import ca.CoursePlanner.ApiWrappers.*;
import ca.CoursePlanner.Logic.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * CoursePlannerController Class, provides the GET and POST mapping
 * required for each endpoint while also converting the model data into
 * ApiWrappers for the front-end. Contains Lists of departments, courses,
 * courseOfferings, sections and watchers and takes in the csv file into
 * our own CsvReader class.
 */

@RestController
public class CoursePlannerController {
    private List<ApiDepartmentWrapper> departments = new ArrayList<>();
    private List<ApiCourseWrapper> courses = new ArrayList<>();
    private List<ApiCourseOfferingWrapper> courseOfferings = new ArrayList<>();
    private List<ApiOfferingSectionWrapper> sections = new ArrayList<>();
    private List<ApiWatcherWrapper> watchers = new ArrayList<>();
    private CsvReader reader = new CsvReader("data/course_data_2018.csv");
    private EntryHandler entryHandler = new EntryHandler(reader);
    private long watcherId = 0;

    @GetMapping("/api/about")
    public ApiAboutWrapper getNames() {
        return new ApiAboutWrapper("Course Offerings WebApp", "Josh Peng and Denzel Tjokroardi");
    }

    @GetMapping("/api/dump-model")
    public void dumpModel() {
        ToDump.printNicelyToTerminal(entryHandler);
    }

    @GetMapping("/api/departments")
    public List<ApiDepartmentWrapper> getAllDepartments() {
        ArrayList<String> subjectList = entryHandler.getSubjectList();
        departments.clear();

        long i = 0;
        for (String subject : subjectList) {
            i++;
            ApiDepartmentWrapper apiDepartmentWrapper = new ApiDepartmentWrapper();
            apiDepartmentWrapper.name = subject;
            apiDepartmentWrapper.deptId = i;
            departments.add(apiDepartmentWrapper);
        }
        return departments;
    }

    @GetMapping("/api/departments/{id}/courses")
    public List<ApiCourseWrapper> getCourse(@PathVariable("id") long departmentId) {
        courses.clear();
        for (ApiDepartmentWrapper departmentWrapper : departments) {
            if (departmentWrapper.deptId == departmentId) {
                entryHandler.categorizeBySubject();
                int id = (int) departmentWrapper.deptId - 1;
                ArrayList<String> listOfCourses = entryHandler.categorizeByCourses().get(id);
                long i = 0;
                for (String course : listOfCourses) {
                    i++;
                    ApiCourseWrapper apiCourseWrapper = new ApiCourseWrapper();
                    apiCourseWrapper.catalogNumber = course;
                    apiCourseWrapper.courseId = i;
                    courses.add(apiCourseWrapper);
                }
            }
        }
        return courses;
    }

    @GetMapping("/api/departments/{id}/courses/{id2}/offerings")
    public List<ApiCourseOfferingWrapper> getOfferings(@PathVariable("id") long departmentId, @PathVariable("id2") long courseId) {
        for (ApiDepartmentWrapper departmentWrapper : departments) {
            if (departmentWrapper.deptId == departmentId) {
                for (ApiCourseWrapper courseWrapper : courses) {
                    if (courseWrapper.courseId == courseId) {
                        courseOfferings.clear();
                        entryHandler.findOfferings(courseWrapper.catalogNumber, departmentWrapper.name);
                        ArrayList<Entry> entries = entryHandler.getOfferingList();
                        long id = 0;
                        for (Entry entry : entries) {
                            ApiCourseOfferingWrapper courseOfferingWrapper = new ApiCourseOfferingWrapper();
                            int year = CourseOffering.computeYear(entry.getSemester());
                            String season = CourseOffering.computeSeason(entry.getSemester());
                            courseOfferingWrapper.courseOfferingId = id;
                            courseOfferingWrapper.instructors = entry.getInstructor();
                            courseOfferingWrapper.location = entry.getLocation();
                            courseOfferingWrapper.semesterCode = entry.getSemester();
                            courseOfferingWrapper.term = season;
                            courseOfferingWrapper.year = year;
                            courseOfferings.add(courseOfferingWrapper);
                            id++;
                        }
                    }
                }
                return courseOfferings;
            }
        }
        throw new IllegalArgumentException();
    }

    @GetMapping("/api/departments/{id}/courses/{id2}/offerings/{id3}")
    public List<ApiOfferingSectionWrapper> getSections(@PathVariable("id") long departmentId, @PathVariable("id2") long courseId, @PathVariable("id3") long offeringId) {
        for (ApiDepartmentWrapper departmentWrapper : departments) {
            if (departmentWrapper.deptId == departmentId) {
                for (ApiCourseWrapper courseWrapper : courses) {
                    if (courseWrapper.courseId == courseId) {
                        for (ApiCourseOfferingWrapper offeringWrapper : courseOfferings) {
                            if (offeringWrapper.courseOfferingId == offeringId) {
                                sections.clear();
                                entryHandler.findSession((int) offeringWrapper.semesterCode, offeringWrapper.instructors, courseWrapper.catalogNumber);
                                ArrayList<Entry> sectionList = entryHandler.getSessions();
                                for (Entry section : sectionList) {
                                    ApiOfferingSectionWrapper sectionWrapper = new ApiOfferingSectionWrapper();
                                    sectionWrapper.enrollmentCap = section.getEnrollCapacity();
                                    sectionWrapper.enrollmentTotal = section.getEnrollTotal();
                                    sectionWrapper.type = section.getComponentCode();
                                    sections.add(sectionWrapper);
                                }
                                return sections;
                            }

                        }
                    }

                }
            }
        }
        throw new IllegalArgumentException();
    }

    @GetMapping("/api/watchers")
    public List<ApiWatcherWrapper> getWatchers() {
        return watchers;
    }

    @PostMapping("/api/watchers")
    @ResponseStatus(HttpStatus.CREATED)
    public List<ApiWatcherWrapper> postWatcher(@RequestBody Watcher watcher) {
        for (ApiDepartmentWrapper departmentWrapper : departments) {
            if (departmentWrapper.deptId == watcher.getDeptId()) {
                for (ApiCourseWrapper courseWrapper : courses) {
                    if (courseWrapper.courseId == watcher.getCourseId()) {
                        ApiWatcherWrapper watcherWrapper = new ApiWatcherWrapper();
                        List<String> event = new ArrayList<>();
                        watcherWrapper.course = courseWrapper;
                        watcherWrapper.department = departmentWrapper;
                        watcherId++;
                        watcherWrapper.id = watcherId;
                        watcherWrapper.events = event;
                        watchers.add(watcherWrapper);
                    }
                }
                return watchers;
            }
        }
        throw new IllegalArgumentException();
    }

    @GetMapping("/api/watchers/{id}")
    public ApiWatcherWrapper getWatcher(@PathVariable("id") long watcherId) {
        for (ApiWatcherWrapper watcherWrapper : watchers) {
            if (watcherWrapper.id == watcherId) {
                return watcherWrapper;
            }
        }
        throw new IllegalArgumentException();
    }


    @PostMapping("/api/addoffering")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiOfferingDataWrapper addNewOffering(@RequestBody Entry entry) {
        ApiOfferingDataWrapper apiOfferingDataWrapper = new ApiOfferingDataWrapper();
        apiOfferingDataWrapper.subjectName = entry.getSubject();
        apiOfferingDataWrapper.catalogNumber = entry.getCatalogueNum();
        apiOfferingDataWrapper.semester = entry.getSemester();
        apiOfferingDataWrapper.component = entry.getComponentCode();
        apiOfferingDataWrapper.enrollmentCap = entry.getEnrollCapacity();
        apiOfferingDataWrapper.instructor = entry.getInstructor();
        apiOfferingDataWrapper.enrollmentTotal = entry.getEnrollTotal();
        apiOfferingDataWrapper.location = entry.getLocation();

        for (ApiWatcherWrapper watcherWrapper : watchers) {
            if (watcherWrapper.department.name.equals(apiOfferingDataWrapper.subjectName)
                    && watcherWrapper.course.catalogNumber.equals(apiOfferingDataWrapper.catalogNumber)) {
                ZoneId zoneId = ZoneId.of("America/Vancouver");
                ZonedDateTime now = ZonedDateTime.now(zoneId);
                DateTimeFormatter newFormat1 = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy");

                int semester = entry.getSemester();
                String season = CourseOffering.computeSeason(semester);
                int year = CourseOffering.computeYear(semester);
                String message = newFormat1.format(now) + ": Added section "
                        + entry.getComponentCode() + " with enrollment ("
                        + entry.getEnrollTotal() + "/" + entry.getEnrollCapacity() + ")"
                        + " to offering " + season + " " + year;
                watcherWrapper.events.add(message);
            }
        }

        return apiOfferingDataWrapper;
    }

    @DeleteMapping("/api/watchers/{id}")
    public void deleteWatcher(@PathVariable("id") long watcherId) {
        for (ApiWatcherWrapper watcherWrapper : watchers) {
            if (watcherWrapper.id == watcherId) {
                watchers.remove(watcherWrapper);
                return;
            }
        }
        throw new IllegalArgumentException();
    }


    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "bad id")
    @ExceptionHandler(IllegalArgumentException.class)
    public void badIdExceptionHandler() {

    }

}


