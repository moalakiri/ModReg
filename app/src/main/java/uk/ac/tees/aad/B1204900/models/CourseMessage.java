package uk.ac.tees.aad.B1204900.models;

import java.util.Date;

public class CourseMessage {
    private String Id, CourseId, Message, Author, DateCreated;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String dateCreated) {
        DateCreated = dateCreated;
    }

    public String getCourseId() {
        return CourseId;
    }

    public void setCourseId(String courseId) {
        CourseId = courseId;
    }
}
