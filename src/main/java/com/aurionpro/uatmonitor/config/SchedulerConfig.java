package com.aurionpro.uatmonitor.config;

import com.aurionpro.uatmonitor.service.UrlMonitorService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class SchedulerConfig {

    private final UrlMonitorService urlMonitorService;

    public SchedulerConfig(UrlMonitorService urlMonitorService) {
        this.urlMonitorService = urlMonitorService;
    }

    /**
     * Run the monitor at 11:00 AM every day
     */
    @Scheduled(cron = "0 0 11 * * ?")
    public void runAt11AM() {
        System.out.println("Running monitor at 11:00 AM...");
        urlMonitorService.checkUrlAndLog();
    }

    /**
     * Run the monitor at 3:00 PM every day
     */
    @Scheduled(cron = "0 0 15 * * ?")
    public void runAt3PM() {
        System.out.println("Running monitor at 3:00 PM...");
        urlMonitorService.checkUrlAndLog();
    }
}
