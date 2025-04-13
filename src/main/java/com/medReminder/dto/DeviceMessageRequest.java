package com.medReminder.dto;

import lombok.Data;

@Data
public class DeviceMessageRequest {
    private String ipAddress;
    private String message;
    private String label;
} 