package com.medReminder.dto;

import lombok.Data;

@Data
public class DeviceIpUpdateRequest {
    private String macAddress;
    private String ipAddress;
} 