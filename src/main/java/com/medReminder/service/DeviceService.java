package com.medReminder.service;

import com.medReminder.entity.Device;

public interface DeviceService {
    Device createDevice(Device device);
    Device getDeviceById(Long id);
    Device getDeviceByPatientId(Long patientId);
    Device updateDevice(Long id, Device device);
    Device updateDeviceStatus(Long id, String status);
    void deleteDevice(Long id);
    Device updateDeviceIpAddress(String deviceId, String ipAddress);
} 