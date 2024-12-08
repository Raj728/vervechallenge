package com.technicalchallenge.service;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.ContentType;
import java.io.IOException;

@Service
public class RequestService {

    private final Set<Integer> uniqueIds = ConcurrentHashMap.newKeySet();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Value("${external.http.timeout}")
    private int httpTimeout;

    public RequestService() {
        scheduler.scheduleAtFixedRate(this::logUniqueCount, 0, 1, TimeUnit.MINUTES);
    }

    public void processRequest(int id, String endpoint) {
        uniqueIds.add(id);

        if (endpoint != null) {
            sendHttpRequest(endpoint, uniqueIds.size());
        }
    }

    private void sendHttpRequest(String endpoint, int count) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(endpoint);

            // Create JSON body
            StringEntity entity = new StringEntity(
                    "{ \"uniqueCount\": " + count + " }",
                    ContentType.APPLICATION_JSON
            );

            request.setEntity(entity);

            // Execute request
            try (CloseableHttpResponse response = client.execute(request)) {
                System.out.println("Response: " + response.getCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void logUniqueCount() {
        System.out.println("Unique Requests in the last minute: " + uniqueIds.size());
        uniqueIds.clear();
    }
}
