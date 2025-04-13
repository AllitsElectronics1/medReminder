package com.medReminder.service.impl;

import com.medReminder.dto.DeviceMessageRequest;
import com.medReminder.service.DeviceMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class DeviceMessageServiceImpl implements DeviceMessageService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void sendMessage(DeviceMessageRequest request) {
        try {
            String url = String.format("http://%s/notify", request.getIpAddress());
            log.info("Sending message to {}: message={}, label={}", 
                request.getIpAddress(), request.getMessage(), request.getLabel());
            
            ResponseEntity<String> response = restTemplate.postForEntity(
                url, 
                request, 
                String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Message sent successfully to {}: {}", 
                    request.getIpAddress(), response.getBody());
            } else {
                log.error("Failed to send message to {}: {}", 
                    request.getIpAddress(), response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error sending message to {}: {}", 
                request.getIpAddress(), e.getMessage(), e);
            throw new RuntimeException("Failed to send message to device", e);
        }
    }
} 