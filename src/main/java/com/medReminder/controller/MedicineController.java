package com.medReminder.controller;

import com.medReminder.entity.Medicine;
import com.medReminder.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.medReminder.repository.PatientRepository;
import com.medReminder.repository.MedicineScheduleRepository;
import com.medReminder.entity.Patient;
import com.medReminder.entity.MedicineSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

import java.util.List;
import com.medReminder.dto.MedicineScheduleCheckResponse;
import lombok.extern.slf4j.Slf4j;
import com.medReminder.entity.TimeOfDay;
import com.medReminder.repository.DeviceRepository;
import com.medReminder.entity.Device;

@Slf4j
@RestController
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
public class MedicineController {
    private final MedicineService medicineService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MedicineScheduleRepository medicineScheduleRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @PostMapping
    public ResponseEntity<Medicine> createMedicine(@RequestBody Medicine medicine) {
        return ResponseEntity.ok(medicineService.createMedicine(medicine));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medicine> getMedicineById(@PathVariable Long id) {
        return ResponseEntity.ok(medicineService.getMedicineById(id));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Medicine>> getMedicinesByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(medicineService.getMedicinesByPatientId(patientId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medicine> updateMedicine(@PathVariable Long id, @RequestBody Medicine medicine) {
        return ResponseEntity.ok(medicineService.updateMedicine(id, medicine));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicine(@PathVariable Long id) {
        medicineService.deleteMedicine(id);
        return ResponseEntity.ok().build();
    }

    private TimeOfDay getTimeOfDay(LocalTime time) {
        int hour = time.getHour();
        if (hour >= 6 && hour < 12) {
            return TimeOfDay.MORNING;
        } else if (hour >= 12 && hour < 18) {
            return TimeOfDay.AFTERNOON;
        } else {
            return TimeOfDay.EVENING;
        }
    }

    @GetMapping("/check-schedule")
    public ResponseEntity<MedicineScheduleCheckResponse> checkMedicineSchedule(@RequestParam String macAddress) {
        log.info("Received check-schedule request for macAddress: {}", macAddress);
        
        // Find device by MAC address
        Optional<Device> deviceOpt = deviceRepository.findByMacAddress(macAddress);
        if (deviceOpt.isEmpty()) {
            log.warn("No device found for macAddress: {}", macAddress);
            return ResponseEntity.badRequest().body(new MedicineScheduleCheckResponse(false, null, null, null));
        }
        
        Device device = deviceOpt.get();
        Patient patient = device.getPatient();
        if (patient == null) {
            log.warn("No patient associated with device macAddress: {}", macAddress);
            return ResponseEntity.badRequest().body(new MedicineScheduleCheckResponse(false, null, null, null));
        }
        
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);
        DayOfWeek today = DayOfWeek.from(java.time.LocalDate.now());
        log.info("Checking schedule for patientId: {}, at time: {}, day: {}", patient.getId(), now, today);
        
        // Check if there's a medicine scheduled for now
        MedicineSchedule currentSchedule = medicineScheduleRepository.findByPatientIdAndActiveAndDayOfWeekAndTime(
                patient.getId(), true, today, now);
        
        if (currentSchedule != null) {
            log.info("Medicine is scheduled for now for patientId: {}", patient.getId());
            String message = currentSchedule.getMedicine().getMedicineName();
            String label = getTimeOfDay(now).toString();
            return ResponseEntity.ok(new MedicineScheduleCheckResponse(true, 0L, message, label));
        } else {
            MedicineSchedule next = medicineScheduleRepository.findFirstByPatientIdAndActiveAndDayOfWeekAndTimeAfterOrderByTimeAsc(
                    patient.getId(), true, today, now);
            if (next != null) {
                long minutes = java.time.temporal.ChronoUnit.MINUTES.between(now, next.getTime());
                log.info("Next medicine schedule for patientId: {} is in {} minutes at {}", patient.getId(), minutes, next.getTime());
                String message = next.getMedicine().getMedicineName();
                String label = getTimeOfDay(next.getTime()).toString();
                return ResponseEntity.ok(new MedicineScheduleCheckResponse(false, minutes, message, label));
            } else {
                log.info("No more medicine schedules for today for patientId: {}", patient.getId());
                return ResponseEntity.ok(new MedicineScheduleCheckResponse(false, null, null, null));
            }
        }
    }
} 