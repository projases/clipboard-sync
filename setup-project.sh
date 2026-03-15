#!/usr/bin/env bash

cat > src/main/java/com/projases/clipboard/ClipboardSyncApplication.java << 'EOF'
package com.projases.clipboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClipboardSyncApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClipboardSyncApplication.class, args);
    }
}
EOF

echo "✅ Created ClipboardSyncApplication.java"
