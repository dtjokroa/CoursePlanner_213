package ca.CoursePlanner.Logic;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Watcher class, used simply as an object to pass into the controller
 * as a Request Body, stores the department ID and course ID to get the
 * targeted course.
 */

public class Watcher {
    @JsonProperty("deptId")
    private int deptId;
    @JsonProperty("courseId")
    private int courseId;

    public Watcher() {

    }
    public long getDeptId() {
        return Integer.toUnsignedLong(deptId);
    }

    public long getCourseId() {
        return Integer.toUnsignedLong(courseId);
    }

}
