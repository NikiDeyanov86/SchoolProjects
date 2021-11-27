package org.elsys.ip.springtimer;


import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.DocFlavor;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "timer", produces = MediaType.APPLICATION_JSON_VALUE)
public class TimerController {
    private static List<Timer> timers = new ArrayList<>();
    private static int active = 0;

    private String randomID() {
        String generatedId = RandomStringUtils.random(3, false, true);

        for(Timer it : timers) {
            if(it.getID().equals(generatedId)) {
                randomID();
            }
        }

        return generatedId;
    }

    /*@PostMapping
    public ResponseEntity<PostResponseBody> createTimer(@RequestBody String request,
                                                        @RequestHeader(value = "TIME-FORMAT", defaultValue = "time") String format) throws JSONException {
            PostResponseBody response = null;
            JSONObject json = new JSONObject(request);
            Timer newTimer;
            String name = null;
            try {
                name = (String) json.get("name");
            } catch (JSONException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            String randomID = randomID();
            int allSeconds = 0;
            boolean validInput = false;
            try {
                if (json.has("hours") && json.has("minutes") && json.has("seconds") && !json.has("time")) {
                    validInput = true;
                    int hours = Integer.parseInt(json.get("hours").toString());
                    int minutes = Integer.parseInt(json.get("minutes").toString());
                    int seconds = Integer.parseInt(json.get("seconds").toString());
                    allSeconds = hours * 3600 + minutes * 60 + seconds;
                } else if (json.has("time") && !json.has("hours") && !json.has("minutes") && !json.has("seconds")) {
                    validInput = true;
                    String[] time = json.get("time").toString().replace("\"", "").split(":", 3);
                    allSeconds = Integer.parseInt(time[0]) * 3600 + Integer.parseInt(time[1]) * 60 + Integer.parseInt(time[2]);
                }
            } catch(NumberFormatException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if(!validInput) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            newTimer = new Timer(randomID, name, Instant.now(), Instant.now().plusSeconds(allSeconds));
            timers.add(newTimer);
            Duration duration = Duration.between(newTimer.getStart(), newTimer.getDuration());

            if(format.equals("time")) {
                response = new PostResponseBody(randomID, name, String.format("%02d:%02d:%02d",
                        duration.toHours(),
                        duration.toMinutesPart(),
                        duration.toSecondsPart()));
            }
            else if(format.equals("seconds")) {
                response = new PostResponseBody(randomID, name, String.format("%d", allSeconds));
            }
            else if(format.equals("hours-minutes-seconds")) {

            }
            return new ResponseEntity<>(response, HttpStatus.CREATED);

    }*/

    @PostMapping
    public ResponseEntity<PostResponseBody> createTimer(@RequestBody String request,
                                                        @RequestHeader(value = "TIME-FORMAT", defaultValue = "time") String format) throws JSONException {

        JSONObject json = new JSONObject(request);
        Timer newTimer;
        String name = null;
        try {
            name = (String) json.get("name");
        } catch (JSONException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String randomID = randomID();
        int allSeconds = 0;
        boolean validInput = false;
        try {
            if (json.has("hours") && json.has("minutes") && json.has("seconds") && !json.has("time")) {
                validInput = true;
                int hours = Integer.parseInt(json.get("hours").toString());
                int minutes = Integer.parseInt(json.get("minutes").toString());
                int seconds = Integer.parseInt(json.get("seconds").toString());
                allSeconds = hours * 3600 + minutes * 60 + seconds;
            } else if (json.has("time") && !json.has("hours") && !json.has("minutes") && !json.has("seconds")) {
                validInput = true;
                String[] time = json.get("time").toString().replace("\"", "").split(":", 3);
                allSeconds = Integer.parseInt(time[0]) * 3600 + Integer.parseInt(time[1]) * 60 + Integer.parseInt(time[2]);
            }
        } catch(NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!validInput) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        newTimer = new Timer(randomID, name, Instant.now(), Instant.now().plusSeconds(allSeconds));
        timers.add(newTimer);
        active++;
        Duration duration = Duration.between(newTimer.getStart(), newTimer.getDuration());

        //return new ResponseEntity<>(new PostResponseBody().build(randomID, name, duration, format), HttpStatus.CREATED);
        return ResponseEntity.created(null).header("ACTIVE-TIMERS", String.format("%d", active)).body(new PostResponseBody().build(randomID, name, duration, format));

    }

    @GetMapping(path = "{id}")
    public ResponseEntity<GetResponseBody> getTimer(@PathVariable String id,
                                                    @RequestParam(name = "long", required = false, defaultValue = "false") String polling,
                                                    @RequestHeader(value = "TIME-FORMAT", defaultValue = "time") String format) throws InterruptedException {
        GetResponseBody response = null;
        boolean found = false;
        String done = "no";
        active = timers.size();
        for(Timer it : timers) {
            if(Duration.between(Instant.now(), it.getDuration()).isZero() || Duration.between(Instant.now(), it.getDuration()).isNegative()) {
                active--;
            }
        }
        for(Timer it : timers) {
            if(it.getID().equals(id)) {
                found = true;
                Duration dur = Duration.between(Instant.now(), it.getDuration());

                if (dur.isNegative() || dur.isZero()) {
                    response = new GetResponseBody().build(it.getID(), it.getName(), Duration.ZERO, "yes", format);
                } else {
                    if(polling.equals("true")) {
                        int flag = 0;
                        for (int i = 1; i <= 10; i++) {
                            TimeUnit.SECONDS.sleep(1);
                            Duration duration = Duration.between(Instant.now(), it.getDuration());
                            if (duration.isNegative() || duration.isZero()) {
                                dur = Duration.ZERO;
                                flag = 1;
                                done = "yes";
                                active--;
                                break;
                            }
                        }
                        if(flag == 0) {
                            dur = Duration.between(Instant.now(), it.getDuration());
                        }
                    }

                    response = new GetResponseBody().build(it.getID(), it.getName(), dur, done, format);
                }
            }
        }
        if(!found) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok().header("ACTIVE-TIMERS", String.format("%d", active)).body(response);
    }
}
