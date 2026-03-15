package com.projases.clipboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClipboardSyncApplication {
    public static void main(String[] args) {
        System.out.println("Starting Clipboard Sync Application...");
        SpringApplication.run(ClipboardSyncApplication.class, args);
    }
}
