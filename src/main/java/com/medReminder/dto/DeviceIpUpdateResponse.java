package com.medReminder.dto;

import lombok.Data;

@Data
    public class DeviceIpUpdateResponse {
    private String deviceSerialNumber;
    private String ipAddress;
    private String status;
} 