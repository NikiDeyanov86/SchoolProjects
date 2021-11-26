package org.elsys.ip.springtimer;

import java.time.Instant;

public class Timer {
    protected String ID;
    protected String name;
    protected Instant start;
    protected Instant duration;

    public Timer(String ID, String name, Instant start, Instant duration) {
        this.ID = ID;
        this.name = name;
        this.start = start;
        this.duration = duration;
    }
}
