package com.medReminder.service.impl;

import com.medReminder.entity.Device;
import com.medReminder.service.DeviceService;
import org.springframework.stereotype.Service;

@Service
public class DeviceServiceImpl implements DeviceService {
    @Override
    public Device createDevice(Device device) {
        // TODO: Implement repository logic
        return device;
    }

    @Override
    public Device getDeviceById(Long id) {
        // TODO: Implement repository logic
        return null;
    }

    @Override
    public Device getDeviceByPatientId(Long patientId) {
        // TODO: Implement repository logic
        return null;
    }

    @Override
    public Device updateDevice(Long id, Device device) {
        // TODO: Implement repository logic
        return device;
    }

    @Override
    public Device updateDeviceStatus(Long id, String status) {
        // TODO: Implement repository logic
        return null;
    }

    @Override
    public void deleteDevice(Long id) {
        // TODO: Implement repository logic
    }
} 