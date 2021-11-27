package org.elsys.ip.springtimer;

import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
public class GetResponseBody {
    private String ID;
    private String name;
    private String time;
    private String done;

    public GetResponseBody(String ID, String name, String remainingTime, String done) {
        this.ID = ID;
        this.name = name;
        this.time = remainingTime;
        this.done = done;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDone() {
        return done;
    }

    public void setDone(String done) {
        this.done = done;
    }
}
