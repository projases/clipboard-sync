package com.projases.clipboard.service;

import com.projases.clipboard.model.ClipboardItem;
import com.projases.clipboard.repository.ClipboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ClipboardService {
    private final ClipboardRepository repository;

    @Autowired
    public ClipboardService(ClipboardRepository repository) {
        this.repository = repository;
    }

    /**
     *   Save content to the database and update the system clipboard
     *   */

    public ClipboardItem saveToClipboard(String content) {
        ClipboardItem item = new ClipboardItem(content);
        ClipboardItem savedItem = repository.save(item);

        System.out.println("Saved clipboard content: " + savedItem);

        // Update system clipboard (Linux)
        updateSystemClipboard(content);

        return savedItem;
    }

    /**
     * Get all clipboard items
     *   */
    public List<ClipboardItem> getHistory() {
        return repository.findAllByOrderByTimestampDesc();
    }
    /**
     * Get the most recent clipboard item
     */
     
    public ClipboardItem getLatest() {
        return repository.findFirstByOrderByTimestampDesc();
    }

    /**
     * Update the system clipboard using xclip (Linux)
     *   */
    private void updateSystemClipboard(String content) {
        try {
            // Try wl-copy first (Wayland)
            ProcessBuilder checkWlCopy = new ProcessBuilder("which", "wl-copy");
            Process checkWl = checkWlCopy.start();
            int wlExists = checkWl.waitFor();
        
            if (wlExists == 0) {
                // Use wl-copy (Wayland)
                ProcessBuilder processBuilder = new ProcessBuilder("wl-copy");
                Process process = processBuilder.start();
                process.getOutputStream().write(content.getBytes());
                process.getOutputStream().close();
                int exitCode = process.waitFor();
            
                if (exitCode == 0) {
                    System.out.println("📋 Updated system clipboard (wl-copy)");
                } else {
                    System.err.println("⚠️ wl-copy exited with code: " + exitCode);
                }
                return;
            }
        
            // Fallback to xclip (X11)
            ProcessBuilder checkXclip = new ProcessBuilder("which", "xclip");
            Process checkX = checkXclip.start();
            int xExists = checkX.waitFor();
        
            if (xExists == 0) {
                ProcessBuilder processBuilder = new ProcessBuilder(
                    "xclip", "-selection", "clipboard"
                );
                Process process = processBuilder.start();
                process.getOutputStream().write(content.getBytes());
                process.getOutputStream().close();
                int exitCode = process.waitFor();
            
                if (exitCode == 0) {
                    System.out.println("📋 Updated system clipboard (xclip)");
                } else {
                    System.err.println("⚠️  xclip exited with code: " + exitCode);
                }
                return;
            }
        
            System.err.println("⚠️  No clipboard tool found - install wl-clipboard or xclip");
        
        } catch (IOException | InterruptedException e) {
            System.err.println("❌ Failed to update system clipboard: " + e.getMessage());
        }
    }
}
