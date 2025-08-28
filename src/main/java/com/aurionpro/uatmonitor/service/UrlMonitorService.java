package com.aurionpro.uatmonitor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class UrlMonitorService {

    private final RestTemplate restTemplate;

    @Value("${monitor.url}")
    private String monitorUrl;

    @Value("${monitor.logfile}")
    private String logFilePath;

    public UrlMonitorService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Check if the URL is accessible and log the result
     */
    public boolean checkUrlAndLog() {
        boolean isUp = false;
        String status;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(monitorUrl, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                isUp = true;
                status = "SUCCESS";
            } else {
                status = "FAILED (Response Code: " + response.getStatusCodeValue() + ")";
            }
        } catch (Exception e) {
            status = "FAILED (Exception: " + e.getMessage() + ")";
        }

        // Format date-time for log
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

        // Write log entry
        try (FileWriter writer = new FileWriter(logFilePath, true)) {
            writer.write(String.format("[%s] URL: %s | Status: %s%n", timestamp, monitorUrl, status));
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }

        return isUp;
    }

    /**
     * Expose monitor URL for API/controller
     */
    public String getMonitorUrl() {
        return monitorUrl;
    }
}
