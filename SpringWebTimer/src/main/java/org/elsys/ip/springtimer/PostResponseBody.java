package org.elsys.ip.springtimer;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
public class PostResponseBody {
    private String ID;
    private String name;
    private String time;

    public PostResponseBody(String ID, String name, String time) {
        this.ID = ID;
        this.name = name;
        this.time = time;
    }

    public String getId() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
