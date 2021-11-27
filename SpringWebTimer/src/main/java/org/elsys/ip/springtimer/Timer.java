package org.elsys.ip.springtimer;

import java.time.Instant;

public class Timer {
    private String ID;
    private String name;
    private Instant start;
    private Instant duration;

    public Timer(String ID, String name, Instant start, Instant duration) {
        this.ID = ID;
        this.name = name;
        this.start = start;
        this.duration = duration;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getStart() {
        return start;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Instant getDuration() {
        return duration;
    }

    public void setDuration(Instant duration) {
        this.duration = duration;
    }
}
