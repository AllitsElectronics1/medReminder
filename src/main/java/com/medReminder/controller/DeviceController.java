package com.medReminder.controller;

import com.medReminder.entity.Device;
import com.medReminder.service.DeviceService;
import com.medReminder.dto.DeviceIpUpdateRequest;
import com.medReminder.dto.DeviceIpUpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceService deviceService;

    @PostMapping
    public ResponseEntity<Device> createDevice(@RequestBody Device device) {
        return ResponseEntity.ok(deviceService.createDevice(device));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Device> getDeviceById(@PathVariable Long id) {
        return ResponseEntity.ok(deviceService.getDeviceById(id));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Device> getDeviceByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(deviceService.getDeviceByPatientId(patientId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Device> updateDevice(@PathVariable Long id, @RequestBody Device device) {
        return ResponseEntity.ok(deviceService.updateDevice(id, device));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Device> updateDeviceStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(deviceService.updateDeviceStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update-ip")
    public ResponseEntity<DeviceIpUpdateResponse> updateDeviceIpAddress(@RequestBody DeviceIpUpdateRequest request) {
        Device device = deviceService.updateDeviceIpAddress(request.getMacAddress(), request.getIpAddress());
        DeviceIpUpdateResponse response = new DeviceIpUpdateResponse();
        response.setDeviceSerialNumber(device.getDeviceSerialNumber());
        response.setIpAddress(device.getIpAddress());
        response.setStatus(device.getStatus().name());
        return ResponseEntity.ok(response);
    }
} 