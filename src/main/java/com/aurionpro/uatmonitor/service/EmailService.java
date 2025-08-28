package com.aurionpro.uatmonitor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;  // ✅ Java 8 uses javax, not jakarta
import java.io.File;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${mail.to}")   // ✅ matches application.properties (not monitor.to)
    private String[] toRecipients;

    @Value("${mail.cc}")   // ✅ matches application.properties (not monitor.cc)
    private String[] ccRecipients;

    @Value("${monitor.logfile}")
    private String logFilePath;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendResultEmail(boolean isUp, String url) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(toRecipients);

            if (ccRecipients != null && ccRecipients.length > 0) {
                helper.setCc(ccRecipients);
            }

            String subject = isUp ? "✅ UAT URL Accessible" : "❌ UAT URL Down";
            String body = buildEmailBody(isUp, url);

            helper.setSubject(subject);
            helper.setText(body, false);

            // Attach log file
            File logFile = new File(logFilePath);
            if (logFile.exists()) {
                FileSystemResource fileResource = new FileSystemResource(logFile);
                helper.addAttachment("url_monitor_log.txt", fileResource);
            }

            mailSender.send(message);
            System.out.println("Email sent successfully!");

        } catch (Exception e) {
            System.err.println("Error while sending email: " + e.getMessage());
        }
    }

    private String buildEmailBody(boolean isUp, String url) {
        String status = isUp ? "UP and accessible ✅" : "DOWN ❌";

        // ✅ Removed Java 15+ text blocks, using old-school concatenation
        return "Dear Team,\n\n"
                + "Public UAT URL is " + (isUp ? "UP" : "DOWN") + ".\n\n"
                + "URL Checked: " + url + "\n"
                + "Status: " + status + "\n\n"
                + "Note: This is with respect to the monitoring status of public UAT URL.\n\n"
                + "This is an automated mail. Do not reply.";
    }
}
