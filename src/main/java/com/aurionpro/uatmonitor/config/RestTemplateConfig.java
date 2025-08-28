package com.aurionpro.uatmonitor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.io.File;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() throws Exception {
        // Load truststore
        SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial(
                        new File("D:/drafts/uatmonitor/canarabank-truststore.jks"),
                        "abcdef".toCharArray(), // truststore password
                        new TrustSelfSignedStrategy())
                .build();

        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(org.apache.http.impl.client.HttpClients.custom()
                .setSSLContext(sslContext)
                .build());

        return new RestTemplate(factory);
    }
}
