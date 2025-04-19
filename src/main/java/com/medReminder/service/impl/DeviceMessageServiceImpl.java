package com.medReminder.service.impl;

import com.medReminder.dto.DeviceMessageRequest;
import com.medReminder.service.DeviceMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.medReminder.dto.DeviceMessageRequestDto;

@Service
@Slf4j
public class DeviceMessageServiceImpl implements DeviceMessageService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void sendMessage(DeviceMessageRequest request) {
        try {
            String url = String.format("http://%s/notify", request.getIpAddress());
            log.info(url);
            log.info("Sending message to {}: message={}, label={}", 
                request.getIpAddress(), request.getMessage(), request.getLabel());
            
            // Create headers and set content type to plain text
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            
            // Create the request entity with plain text message
            String messageText = request.getMessage() + "|" + request.getLabel();
            HttpEntity<String> requestEntity = new HttpEntity<>(messageText, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
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