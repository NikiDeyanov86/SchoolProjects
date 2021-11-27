package org.elsys.ip.springtimer;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Duration;

@ResponseBody
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponseBody {
    private String ID;
    private String name;
    private String time;
    private String totalSeconds;
    private String hours;
    private String minutes;
    private String seconds;

    public PostResponseBody(String ID, String name, String time, String totalSeconds, String hour, String minute, String seconds) {
        this.ID = ID;
        this.name = name;
        this.time = time;
        this.totalSeconds = totalSeconds;
        this.hours = hour;
        this.minutes = minute;
        this.seconds = seconds;
    }
    public PostResponseBody() {

    }

    public PostResponseBody build(String ID, String name, Duration time, String atr) {
        if(atr.equals("seconds")) {
            return new PostResponseBody(ID, name, null, String.format("%d", time.toSeconds()), null, null, null);
        }
        else if(atr.equals("hours-minutes-seconds")) {
            return new PostResponseBody(ID, name, null, null, String.format("%02d", time.toHours()),
                    String.format("%02d", time.toMinutesPart()), String.format("%02d", time.toSecondsPart()));
        }
        return new PostResponseBody(ID, name, String.format("%02d:%02d:%02d",
                time.toHours(),
                time.toMinutesPart(),
                time.toSecondsPart()), null, null, null, null);
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

    public String getTotalSeconds() {
        return totalSeconds;
    }

    public void setTotalSeconds(String totalSeconds) {
        this.totalSeconds = totalSeconds;
    }

    public String getHour() {
        return hours;
    }

    public void setHour(String hour) {
        this.hours = hour;
    }

    public String getMinute() {
        return minutes;
    }

    public void setMinute(String minute) {
        this.minutes = minute;
    }

    public String getSeconds() {
        return seconds;
    }

    public void setSeconds(String seconds) {
        this.seconds = seconds;
    }
}
