package com.projases.clipboard.controller;

import com.projases.clipboard.model.ClipboardItem;
import com.projases.clipboard.service.ClipboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allow requests from any origin (for development purposes)
                        
public class ClipboardController {
    private final ClipboardService clipboardService;

    @Value("${app.api.key.default-insecure-key}")
    private String apiKey;

    @Autowired
    public ClipboardController(ClipboardService clipboardService) {
        this.clipboardService = clipboardService;
    }

    /**
     * POST /api/copy - Send clipboard content to server
     * 
     * Request body: {"content": "your text here"}
     * Headers: Authorization: Bearer YOUR_API_KEY
     */
     *
    @PostMapping("/copy")
    public ResponseEntity<ClipboardItem> copyToClipboard(
        @RequestHeader(value = "Authorization", required = false) String authHeader,
        @RequestBody Map<String, String> payload) {

        // Validate API key
        if (!isValidAuth(authHeader)) {
            System.out.println("Unauthorized request - invalid API key");
            return ResponseEntity.status(401).build();
        }


        String content = payload.get("content");
        if (content == null || content.trim().isEmpty()) {
            System.out.println("Bad request - content is missing or empty");
            return ResponseEntity.badRequest().build();
        }

        System.out.println("📥 Received clipboard content (" + content.length() + " chars)");
        
        ClipboardItem item = clipboardService.saveToClipboard(content);
        return ResponseEntity.ok(item);
    }

    /**
     * GET /api/latest - Get most recent clipboard item
     * 
     * Headers: Authorization: Bearer YOUR_API_KEY
     */
    @GetMapping("/latest")
    public ResponseEntity<ClipboardItem> getLatest(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        if (!isValidAuth(authHeader)) {
            System.err.println("❌ Unauthorized request - invalid API key");
            return ResponseEntity.status(401).build();
        }
        
        ClipboardItem item = clipboardService.getLatest();
        
        if (item == null) {
            System.out.println("ℹ️  No clipboard items found");
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        
        System.out.println("📤 Sending latest clipboard item: " + item);
        return ResponseEntity.ok(item);
    }

    /**
     * GET /api/history - Retrieve clipboard history (requires authentication)
     *   */

    @GetMapping("/history")
    public ResponseEntity<List<ClipboardItem>> getHistory(
        @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (!isValidAuth(authHeader)) {
            System.out.println("Unauthorized request - invalid API key");
            return ResponseEntity.status(401).build();
        }

        List<ClipboardItem> history = clipboardService.getHistory();
        System.out.println("📜 Returning clipboard history (" + history.size() + " items)");
        return ResponseEntity.ok(history);
    }
    /**
     * GET /api/health - Health check endpoint (no authentication required)
     *  */

    @GetMapping("/health")
    public ResponseEntity<Map<String,String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "Clipboard Sync",
            "version", "0.1.0"
        ));
        
    }

    /**
     * Validate the authorization header
     *   */
    private boolean isValidAuth(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }
        String token = authHeader.substring(7); // Remove "Bearer " prefix
        return token.equals(apiKey);
    }

}
