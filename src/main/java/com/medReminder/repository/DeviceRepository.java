package com.medReminder.repository;

import com.medReminder.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByMacAddress(String macAddress);
    Optional<Device> findByPatientId(Long patientId);
    List<Device> findAllByPatientId(Long patientId);
    boolean existsByDeviceSerialNumber(String deviceSerialNumber);
} 