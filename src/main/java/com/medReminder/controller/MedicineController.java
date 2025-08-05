package com.medReminder.controller;

import com.medReminder.entity.Medicine;
import com.medReminder.service.MedicineService;
import com.medReminder.service.UserService;
import com.medReminder.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.medReminder.repository.PatientRepository;
import com.medReminder.repository.MedicineRepository;
import com.medReminder.repository.MedicineScheduleRepository;
import com.medReminder.entity.Patient;
import com.medReminder.entity.MedicineSchedule;
import com.medReminder.entity.User;
import com.medReminder.entity.Device;
import com.medReminder.entity.DeviceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

import java.util.List;
import com.medReminder.dto.MedicineScheduleCheckResponse;
import com.medReminder.dto.MedicineRequest;
import lombok.extern.slf4j.Slf4j;
import com.medReminder.entity.TimeOfDay;
import com.medReminder.repository.DeviceRepository;
import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class MedicineController {
    private final MedicineService medicineService;
    private final UserService userService;
    private final DeviceService deviceService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private MedicineScheduleRepository medicineScheduleRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<Medicine> createMedicine(@Valid @RequestBody MedicineRequest request) {
        log.info("Creating medicine: {} for patient ID: {}", request.getMedicineName(), request.getPatientId());
        
        Patient patient;
        
        // Handle patient selection
        if (request.getPatientId() != null) {
            // Android app: specific patient ID provided
            Optional<Patient> patientOpt = patientRepository.findById(request.getPatientId());
            if (patientOpt.isEmpty()) {
                log.error("Medicine creation failed: patient not found with ID: {}", request.getPatientId());
                return ResponseEntity.badRequest().build();
            }
            patient = patientOpt.get();
        } else {
            // Web app: use current user's first patient
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            
            List<Patient> userPatients = patientRepository.findByUserEmail(currentUserEmail);
            if (userPatients.isEmpty()) {
                log.error("Medicine creation failed: no patients found for user: {}", currentUserEmail);
                return ResponseEntity.badRequest().body(null);
            }
            
            patient = userPatients.get(0); // Use first patient
            log.info("Using first patient for user: {} - Patient: {}", currentUserEmail, patient.getName());
        }
        
        // Handle device creation/update if deviceId is provided
        if (request.getDeviceId() != null && !request.getDeviceId().trim().isEmpty()) {
            try {
                // Update patient's device ID
                patient.setDeviceId(request.getDeviceId());
                patient = patientRepository.save(patient);
                log.info("Updated patient device ID: {}", request.getDeviceId());
                
                // Create or update device
                Device existingDevice = deviceService.getDeviceByPatientId(patient.getId());
                if (existingDevice != null) {
                    // Update existing device
                    existingDevice.setDeviceSerialNumber(request.getDeviceId());
                    deviceService.updateDevice(existingDevice.getDeviceId(), existingDevice);
                    log.info("Updated existing device for patient: {}", patient.getName());
                } else {
                    // Create new device
                    Device device = new Device();
                    device.setPatient(patient);
                    device.setDeviceSerialNumber(request.getDeviceId());
                    device.setStatus(DeviceStatus.ACTIVE);
                    device.setLastSyncTime(LocalDateTime.now()); // Required field
                    deviceService.createDevice(device);
                    log.info("Created new device for patient: {}", patient.getName());
                }
            } catch (Exception e) {
                log.error("Failed to create/update device: {}", e.getMessage(), e);
                // Continue with medicine creation even if device fails
            }
        }
        
        // Create the medicine
        Medicine medicine = new Medicine();
        medicine.setPatient(patient);
        medicine.setMedicineName(request.getMedicineName());
        medicine.setDosage(request.getDosage());
        medicine.setStartDate(request.getStartDate());
        medicine.setEndDate(request.getEndDate());
        
        medicine = medicineRepository.save(medicine);
        log.info("Created medicine: {} for patient: {}", medicine.getMedicineName(), patient.getName());
        
        // Create medicine schedules if schedule information is provided
        if (request.getDaysOfWeek() != null && !request.getDaysOfWeek().isEmpty() && request.getTime() != null) {
            try {
                log.info("Creating {} schedules for medicine: {}", request.getDaysOfWeek().size(), medicine.getMedicineName());
                for (DayOfWeek dayOfWeek : request.getDaysOfWeek()) {
                    MedicineSchedule schedule = new MedicineSchedule();
                    schedule.setMedicine(medicine);
                    schedule.setPatient(patient);
                    schedule.setDayOfWeek(dayOfWeek);
                    schedule.setTime(request.getTime());
                    schedule.setReminderMinutesBefore(request.getReminderMinutesBefore());
                    schedule.setActive(true);
                    MedicineSchedule savedSchedule = medicineScheduleRepository.save(schedule);
                    log.info("Created schedule ID: {} for {} at {} on {}", 
                        savedSchedule.getId(), medicine.getMedicineName(), request.getTime(), dayOfWeek);
                }
                log.info("Successfully created {} schedules", request.getDaysOfWeek().size());
            } catch (Exception e) {
                log.error("Failed to create schedules: {}", e.getMessage(), e);
                // Continue even if schedule creation fails
            }
        } else {
            log.info("No schedule information provided, skipping schedule creation");
        }
        
        log.info("Successfully created medicine with schedules for patient: {}", patient.getName());
        return ResponseEntity.ok(medicine);
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