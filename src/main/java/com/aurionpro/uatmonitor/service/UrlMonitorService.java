package com.aurionpro.uatmonitor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class UrlMonitorService {

    @Value("${monitor.url}")
    private String monitorUrl;

    @Value("${monitor.logfile}")
    private String logFilePath;

    /**
     * Check if the URL is accessible and log the result
     */
    public boolean checkUrlAndLog() {
        boolean isUp = false;
        String status;

        HttpURLConnection connection = null;
        try {
            URL url = new URL(monitorUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000); // 15 sec timeout
            connection.setReadTimeout(15000);

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                isUp = true;
                status = "SUCCESS";
            } else {
                status = "FAILED (Response Code: " + responseCode + ")";
            }

        } catch (Exception e) {
            status = "FAILED (Exception: " + e.getMessage() + ")";
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        // Format date-time for log
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

        // Write log entry
        try (FileWriter writer = new FileWriter(logFilePath, true)) {
            writer.write(String.format("[%s] URL: %s | Status: %s\r\n", timestamp, monitorUrl, status));
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }

        return isUp;
    }
}
