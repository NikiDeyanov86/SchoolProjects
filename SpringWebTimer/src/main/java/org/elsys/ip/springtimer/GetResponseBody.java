package org.elsys.ip.springtimer;

import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
public class GetResponseBody {
    public String ID;
    public String name;
    public String time;
    public String done;

    public GetResponseBody(String ID, String name, String remainingTime, String done) {
        this.ID = ID;
        this.name = name;
        this.time = remainingTime;
        this.done = done;
    }
}
