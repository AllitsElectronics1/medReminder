package com.medReminder.controller;

import com.medReminder.entity.Device;
import com.medReminder.service.DeviceService;

import io.micrometer.core.ipc.http.HttpSender.Response;

import com.medReminder.dto.DeviceIpUpdateRequest;
import com.medReminder.dto.DeviceIpUpdateResponse;
import com.medReminder.dto.UpdateMedicineStatusRequest;
import com.medReminder.dto.UpdateMedicineStatusResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
@Slf4j
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

    @PostMapping("/updateMedicineStatus")
    public ResponseEntity<UpdateMedicineStatusResponse> updateMedicineStatus(@RequestBody UpdateMedicineStatusRequest request) {
        log.info("Updating medicine status for device: {}", request.getMacAddress());
        UpdateMedicineStatusResponse response = new UpdateMedicineStatusResponse();
        response.setMessage("Medicine status updated successfully");
        response.setStatus("success");
        return ResponseEntity.ok(response);
    }

    
} 