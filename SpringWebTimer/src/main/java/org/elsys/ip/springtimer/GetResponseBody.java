package org.elsys.ip.springtimer;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Duration;

@ResponseBody
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetResponseBody {
    private String ID;
    private String name;
    private String time;
    private String totalSeconds;
    private String hours;
    private String minutes;
    private String seconds;
    private String done;


    public GetResponseBody(String ID, String name, String remainingTime, String totalSeconds, String h, String m, String s, String done) {
        this.ID = ID;
        this.name = name;
        this.time = remainingTime;
        this.totalSeconds = totalSeconds;
        this.hours = h;
        this.minutes = m;
        this.seconds = s;
        this.done = done;
    }

    public GetResponseBody() {

    }
    public GetResponseBody build(String ID, String name, Duration time, String done ,String atr) {
        if(atr.equals("seconds")) {
            return new GetResponseBody(ID, name, null, String.format("%d", time.toSeconds()), null, null, null, done);
        }
        else if(atr.equals("hours-minutes-seconds")) {
            return new GetResponseBody(ID, name, null, null, String.format("%02d", time.toHours()),
                    String.format("%02d", time.toMinutesPart()), String.format("%02d", time.toSecondsPart()), done);
        }
        return new GetResponseBody(ID, name, String.format("%02d:%02d:%02d",
                time.toHours(),
                time.toMinutesPart(),
                time.toSecondsPart()), null, null, null, null, done);
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

    public String getTotalSeconds() {
        return totalSeconds;
    }

    public void setTotalSeconds(String totalSeconds) {
        this.totalSeconds = totalSeconds;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hour) {
        this.hours = hour;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minute) {
        this.minutes = minute;
    }

    public String getSeconds() {
        return seconds;
    }

    public void setSeconds(String seconds) {
        this.seconds = seconds;
    }
}
