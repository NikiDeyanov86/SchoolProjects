package org.elsys.ip.springtimer;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
public class PostResponseBody {
    public String ID;
    public String name;
    public String time;

    public PostResponseBody(String ID, String name, String time) {
        this.ID = ID;
        this.name = name;
        this.time = time;
    }
}
