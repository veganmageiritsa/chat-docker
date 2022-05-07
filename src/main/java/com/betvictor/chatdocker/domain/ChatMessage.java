package com.betvictor.chatdocker.domain;

import java.time.Instant;
import java.util.Objects;

import org.springframework.data.annotation.Id;

public class ChatMessage {

    @Id
    private String id;
    private String name;
    private String message;
    private Instant date;
    
    
    public String getId() {
        return id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
    
    public Instant getDate() {
        return date;
    }
    
    public void setDate(final Instant date) {
        this.date = date;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChatMessage that = (ChatMessage) o;
        return id.equals(that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "ChatMessage{" +
               "Id='" + id + '\'' +
               ", name='" + name + '\'' +
               ", message='" + message + '\'' +
               ", date=" + date +
               '}';
    }
    
}
