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

    public ClipboardItem saveClipboardContent(String content) {
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
            // Check if xclip is available
            Process checkBuilder = new ProcessBuilder("which", "xclip");
            Process checkProcess = checkBuilder.start();
            int checkCode = checkProcess.waitFor();

            if (checkCode != 0) {
                System.err.println("xclip is not installed. Please install xclip to enable clipboard functionality.");
                return;
            }

            // Use xclip to update clipboard
            ProcessBuilder processBuilder = new ProcessBuilder("xclip", "-selection", "clipboard");
            Process process = processBuilder.start();

            // Write content to xclip's stdin
            process.getOutputStream().write(content.getBytes());
            process.getOutputStream().close();

            // Wait for the process to complete
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("System clipboard updated successfully.");
            } else {
                System.err.println("⚠️Failed to update system clipboard. Exit code: " + exitCode);
            }
                
        } catch (IOException | InterruptedException e) {
            System.err.println("Error updating system clipboard: " + e.getMessage());
            // Don't throw - clipboard update is optional
        }

}
