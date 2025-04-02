package com.medReminder.service.impl;

import com.medReminder.dto.PatientCreationRequest;
import com.medReminder.entity.Patient;
import com.medReminder.entity.Medicine;
import com.medReminder.entity.MedicineSchedule;
import com.medReminder.repository.PatientRepository;
import com.medReminder.repository.MedicineRepository;
import com.medReminder.repository.MedicineScheduleRepository;
import com.medReminder.service.PatientService;
import com.medReminder.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final MedicineRepository medicineRepository;
    private final MedicineScheduleRepository medicineScheduleRepository;
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public List<Patient> getPatientsForCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug("Fetching patients for user with email: {}", email);
        List<Patient> patients = patientRepository.findByUserEmail(email);
        log.debug("Found {} patients", patients.size());
        
        // Sort patients by ID
        patients.sort(Comparator.comparing(Patient::getId));
        
        // Initialize the schedules collection and ensure they are loaded
        patients.forEach(patient -> {
            List<MedicineSchedule> medicineSchedules = medicineScheduleRepository.findByPatientId(patient.getId());
            log.debug("Patient {} has {} schedules", patient.getName(), medicineSchedules.size());
            medicineSchedules.forEach(schedule -> {
                log.debug("Schedule: Medicine={}, Dosage={}, Time={}, Day={}, Active={}",
                    schedule.getMedicine().getMedicineName(),
                    schedule.getMedicine().getDosage(),
                    schedule.getTime(),
                    schedule.getDayOfWeek(),
                    schedule.isActive());
            });
        });
        
        return patients;
    }

    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    @Override
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Patient createPatient(PatientCreationRequest request) {
        log.debug("Creating new patient with {} medicine schedules", request.getMedicineSchedules().size());
        
        // Create and save patient
        Patient patient = new Patient();
        patient.setName(request.getName());
        patient.setDeviceId(request.getDeviceId());
        patient = patientRepository.save(patient);

        // Process each medicine schedule
        for (PatientCreationRequest.MedicineScheduleDTO scheduleDTO : request.getMedicineSchedules()) {
            // Create and save medicine
            Medicine medicine = new Medicine();
            medicine.setMedicineName(scheduleDTO.getMedicineName());
            medicine.setDosage(scheduleDTO.getDosage());
            medicine.setPatient(patient);
            medicine.setStartDate(LocalDate.now());
            medicine = medicineRepository.save(medicine);

            // Create schedule for each selected day
            for (DayOfWeek dayOfWeek : scheduleDTO.getDaysOfWeek()) {
                MedicineSchedule schedule = new MedicineSchedule();
                schedule.setMedicine(medicine);
                schedule.setPatient(patient);
                schedule.setDayOfWeek(dayOfWeek);
                schedule.setTime(scheduleDTO.getTime());
                schedule.setReminderMinutesBefore(scheduleDTO.getReminderMinutesBefore());
                schedule.setActive(true);
                medicineScheduleRepository.save(schedule);
            }
        }

        log.debug("Successfully created patient with ID: {}", patient.getId());
        return patientRepository.findById(patient.getId()).orElseThrow();
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
} 