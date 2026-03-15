package com.projases.clipboard.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "clipboard_items")

public class ClipboardItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10000)
   private String content;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public ClipboardItem() {
        this.timestamp = LocalDateTime.now();
    }

    public ClipboardItem(String content) {
        this.content = content;
        this.timestamp = LocalDateTime.now(); 
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
@Override
    public String toString() {
        return "ClipboardItem{" +
                "id=" + id +
                ", content='" + (content.length() > 50 ? content.substring(0, 50) + "..." : content) + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
