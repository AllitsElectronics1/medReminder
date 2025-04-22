package com.medReminder.repository;

import com.medReminder.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByMacAddress(String macAddress);
    boolean existsByDeviceSerialNumber(String deviceSerialNumber);
} 