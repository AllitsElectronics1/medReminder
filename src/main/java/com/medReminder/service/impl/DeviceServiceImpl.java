package com.medReminder.service.impl;

import com.medReminder.entity.Device;
import com.medReminder.service.DeviceService;
import com.medReminder.repository.DeviceRepository;
import com.medReminder.exception.DeviceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {
    private final DeviceRepository deviceRepository;

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

    @Override
    public Device updateDeviceIpAddress(String macAddress, String ipAddress) {
        Device device = deviceRepository.findByMacAddress(macAddress)
            .orElseThrow(() -> new DeviceNotFoundException("Device not found with mac address: " + macAddress));

        device.setIpAddress(ipAddress);
        return deviceRepository.save(device);
    }
} 