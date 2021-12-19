package org.elsys.ip.exam;

public class Note {
    private String text;
    private String id;

    public Note(String text, String id) {
        this.text = text;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setId(String id) {
        this.id = id;
    }
}
