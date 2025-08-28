package com.aurionpro.uatmonitor.controller;

import com.aurionpro.uatmonitor.service.UrlMonitorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UrlMonitorController {

    private final UrlMonitorService urlMonitorService;

    public UrlMonitorController(UrlMonitorService urlMonitorService) {
        this.urlMonitorService = urlMonitorService;
    }

    @GetMapping("/api/status")
    public Map<String, Object> checkUrlStatus() {
        boolean isUp = urlMonitorService.checkUrlAndLog();

        Map<String, Object> response = new HashMap<>();
        response.put("url", urlMonitorService.getMonitorUrl());
        response.put("status", isUp ? "UP" : "DOWN");
        response.put("timestamp", LocalDateTime.now().toString());

        return response;
    }
}
