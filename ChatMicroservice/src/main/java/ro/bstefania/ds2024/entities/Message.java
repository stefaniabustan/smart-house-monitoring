package ro.bstefania.ds2024.entities;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Message {
    private static final long serialVersionUID = 1L;

    private String sender;

    private String recipient;

    private String content;

    private LocalDateTime timestamp;

    private boolean isRead;
    private boolean groupChat = false; //true if sent in group chat

    private List<String> readBy = new ArrayList<>();

    public Message( String sender, String recipient, String content, LocalDateTime timestamp, boolean isRead, boolean groupChat) {

        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.timestamp = timestamp;
        this.isRead = isRead;
        this.groupChat = groupChat;
    }

    public Message() {

    }

    public Message(String sender, String recipient, String content, LocalDateTime timestamp, boolean isGroupChat) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.timestamp = timestamp;
        this.groupChat = isGroupChat;

    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean read) {
        isRead = read;
    }

    public boolean getIsGroupChat() {
        return groupChat;
    }

    public void setIsGroupChat(boolean groupChat) {
        this.groupChat = groupChat;
    }

    public List<String> getReadBy() {
        return readBy;
    }

    public void setReadBy(List<String> readBy) {
        this.readBy = readBy;
    }

    @Override
    public String toString() {
        return "Message{" +
                ", sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                ", isRead=" + isRead +
                ", groupChat=" + groupChat +
                '}';
    }
}