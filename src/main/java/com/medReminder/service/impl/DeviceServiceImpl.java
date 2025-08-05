package com.medReminder.service.impl;

import com.medReminder.entity.Device;
import com.medReminder.entity.DeviceStatus;
import com.medReminder.service.DeviceService;
import com.medReminder.repository.DeviceRepository;
import com.medReminder.exception.DeviceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {
    private final DeviceRepository deviceRepository;

    @Override
    public Device createDevice(Device device) {
        if (device == null) {
            throw new IllegalArgumentException("Device cannot be null");
        }
        
        // Set default values if not provided
        if (device.getStatus() == null) {
            device.setStatus(DeviceStatus.ACTIVE);
        }
        
        if (device.getLastSyncTime() == null) {
            device.setLastSyncTime(LocalDateTime.now());
        }
        
        return deviceRepository.save(device);
    }

    @Override
    public Device getDeviceById(Long id) {
        return deviceRepository.findById(id)
            .orElseThrow(() -> new DeviceNotFoundException("Device not found with id: " + id));
    }

    @Override
    public Device getDeviceByPatientId(Long patientId) {
        return deviceRepository.findByPatientId(patientId)
            .orElse(null);
    }

    @Override
    public List<Device> getDevicesByPatientId(Long patientId) {
        return deviceRepository.findAllByPatientId(patientId);
    }

    @Override
    public Device updateDevice(Long id, Device device) {
        Device existingDevice = getDeviceById(id);
        
        existingDevice.setDeviceSerialNumber(device.getDeviceSerialNumber());
        existingDevice.setIpAddress(device.getIpAddress());
        existingDevice.setStatus(device.getStatus());
        existingDevice.setLastSyncTime(device.getLastSyncTime());
        
        return deviceRepository.save(existingDevice);
    }

    @Override
    public Device updateDeviceStatus(Long id, String status) {
        Device device = getDeviceById(id);
        device.setStatus(DeviceStatus.valueOf(status.toUpperCase()));
        return deviceRepository.save(device);
    }

    @Override
    public void deleteDevice(Long id) {
        if (!deviceRepository.existsById(id)) {
            throw new DeviceNotFoundException("Device not found with id: " + id);
        }
        deviceRepository.deleteById(id);
    }

    @Override
    public Device updateDeviceIpAddress(String macAddress, String ipAddress) {
        Device device = deviceRepository.findByMacAddress(macAddress)
            .orElseThrow(() -> new DeviceNotFoundException("Device not found with mac address: " + macAddress));

        device.setIpAddress(ipAddress);
        return deviceRepository.save(device);
    }
} 