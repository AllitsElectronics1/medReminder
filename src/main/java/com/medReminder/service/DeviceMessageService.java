package com.medReminder.service;

import com.medReminder.dto.DeviceMessageRequest;

public interface DeviceMessageService {
    void sendMessage(DeviceMessageRequest request);
} 