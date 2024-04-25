package org.JavviFdeez.model;

public class Courses {

    // Attributes
    private int course_id;
    private int contact_id;
    private String name;
    private String duration;
    private int position;

    // Constructor
    public Courses(int course_id, int contact_id, String name, String duration, int position) {
        this.course_id = course_id;
        this.contact_id = contact_id;
        this.name = name;
        this.duration = duration;
        this.position = position;
    }

    // Getters and Setters
    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public int getContact_id() {
        return contact_id;
    }

    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}