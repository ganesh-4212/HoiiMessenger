package com.kempasolutions.app.hoiimessanger;

import java.util.Date;

/**
 * Created by Ganesh Poojary on 7/31/2016.
 */
public class ChatMessage {
    private String from;
    private String to;
    private MessageType type;
    private MessageStatus status;
    private String time;
    private Object body;
    private boolean isSelected;

    public ChatMessage(String to, String from, MessageType type, Object body, String time, MessageStatus status) {
        this.to = to;
        this.from = from;
        this.type = type;
        this.body = body;
        this.time = time;
        this.status = status;
        isSelected=false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public MessageStatus getStatus() {
        return status;
    }
    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}
