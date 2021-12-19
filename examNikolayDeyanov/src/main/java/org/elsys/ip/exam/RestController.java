package org.elsys.ip.exam;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@org.springframework.web.bind.annotation.RestController
@RequestMapping(value="notes", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestController {

    static List<Note> notes = new ArrayList<>();

    private String randomID() {
        String generatedId = RandomStringUtils.random(3, false, true);

        for(Note it : notes) {
            if(it.getId().equals(generatedId)) {
                randomID();
            }
        }

        return generatedId;
    }

    @PostMapping
    public ResponseEntity<String> createNote(@RequestBody String request) throws JSONException {

        JSONObject json = new JSONObject(request);
        String id = randomID();
        if(!json.has("text")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String text = json.get("text").toString();
        Note newNote = new Note(text, id);
        notes.add(newNote);

        return ResponseEntity.created(null).body(id);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Note> readNote(@PathVariable String id,
                                         @RequestHeader(value="char-case", defaultValue = "none",required = false) String header) {

        Note response = null;
        for(Note it : notes) {
            if(it.getId().equals(id)) {
                response = it;
            }
        }
        if(response == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(header.equals("uppercase")) {
            response.setText(response.getText().toUpperCase(Locale.ROOT));
        }
        else if(header.equals("lowercase")) {
            response.setText(response.getText().toLowerCase(Locale.ROOT));
        }

        return ResponseEntity.ok().body(response);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<Note> changeNote(@PathVariable String id,
                                           @RequestBody String request) throws JSONException {

        JSONObject json = new JSONObject(request);
        Note response = null;
        String previousText;
        for(Note it : notes) {
            if(it.getId().equals(id)) {
                response = it;
            }
        }
        if(response == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(json.has("text")) {
            previousText = response.getText();
            response.setText(json.get("text").toString());
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().header("previous", previousText).body(response);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity deleteNote(@PathVariable String id) {

        boolean found = false;
        for(Note it : notes) {
            if(it.getId().equals(id)) {
                found = true;
                notes.remove(it);
                break;
            }
        }
        if(!found) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.noContent().build();
    }
}
