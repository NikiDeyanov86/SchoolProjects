package org.elsys.ip.springtimer;



import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang.RandomStringUtils;

import java.sql.Time;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "timer", produces = MediaType.APPLICATION_JSON_VALUE)
public class TimerController {
    private static List<Timer> timers = new ArrayList<>();

    private String randomID() {
        String generatedId = RandomStringUtils.random(3, false, true);

        for(Timer it : timers) {
            if(it.ID.equals(generatedId)) {
                randomID();
            }
        }

        return "<" + generatedId + ">";
    }

    @PostMapping
    public PostResponseBody createTimer(@RequestBody String request) throws JSONException {
            JSONObject json = new JSONObject(request);
            Timer newTimer;
            String name = (String) json.get("name");
            String randomID = randomID();
            int allSeconds = 0;
            if (json.has("hours") && json.has("minutes") && json.has("seconds")) {
                Integer hours = (Integer) json.get("hours");
                Integer minutes = (Integer) json.get("minutes");
                Integer seconds = (Integer) json.get("seconds");
                allSeconds = hours*3600 + minutes*60 + seconds;
            }
            else if(json.has("time")) {
                String[] time = json.get("time").toString().replace("\"", "").split(":", 3);
                allSeconds = Integer.parseInt(time[0])*3600 + Integer.parseInt(time[1])*60 + Integer.parseInt(time[2]);
            }

            newTimer = new Timer(randomID, name, Instant.now(), Instant.now().plusSeconds(allSeconds));
            timers.add(newTimer);
            Duration duration = Duration.between(newTimer.start, newTimer.duration);

            return new PostResponseBody(randomID, name, String.format("%02d:%02d:%02d",
                    duration.toHours(),
                    duration.toMinutesPart(),
                    duration.toSecondsPart()));
    }

    @GetMapping(path = "{id}")
    public GetResponseBody getTimer(@PathVariable String id, @RequestParam(name = "long", required = false, defaultValue = "false") String polling) throws InterruptedException {
        GetResponseBody response = null;
        for(Timer it : timers) {
            if(it.ID.equals(id)) {

                Duration dur = Duration.between(Instant.now(), it.duration);

                if (dur.isNegative() || dur.isZero()) {
                    response = new GetResponseBody(it.ID, it.name, "00:00:00", "yes");
                } else {
                    if(polling.equals("true")) {
                        int flag = 0;
                        for (int i = 1; i <= 10; i++) {
                            TimeUnit.SECONDS.sleep(1);
                            Duration duration = Duration.between(Instant.now(), it.duration);
                            if (duration.isNegative() || duration.isZero()) {
                                dur = Duration.ZERO;
                                flag = 1;
                                break;
                            }
                        }
                        if(flag == 0) {
                            dur = Duration.between(Instant.now(), it.duration);
                        }
                    }
                    response = new GetResponseBody(it.ID, it.name, String.format("%02d:%02d:%02d",
                            dur.toHours(),
                            dur.toMinutesPart(),
                            dur.toSecondsPart()), "no");
                }
            }
        }

        return response;
    }
}
