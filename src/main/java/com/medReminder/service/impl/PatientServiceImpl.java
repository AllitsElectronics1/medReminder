package com.medReminder.service.impl;

import com.medReminder.dto.PatientCreationRequest;
import com.medReminder.entity.Patient;
import com.medReminder.entity.Medicine;
import com.medReminder.entity.MedicineSchedule;
import com.medReminder.entity.User;
import com.medReminder.entity.Device;
import com.medReminder.entity.BoxActivity;
import com.medReminder.entity.Report;
import com.medReminder.entity.Reminder;
import com.medReminder.repository.PatientRepository;
import com.medReminder.repository.MedicineRepository;
import com.medReminder.repository.MedicineScheduleRepository;
import com.medReminder.service.PatientService;
import com.medReminder.service.UserService;
import com.medReminder.service.DeviceService;
import com.medReminder.service.ReportService;
import com.medReminder.service.BoxActivityService;
import com.medReminder.service.ReminderService;
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
    private final DeviceService deviceService;
    private final ReportService reportService;
    private final BoxActivityService boxActivityService;
    private final ReminderService reminderService;

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
    @Transactional
    public void deletePatient(Long id) {
        log.info("Deleting patient with ID: {}", id);
        
        // Get the patient first to check existence
        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));

        // Delete associated device and its activities
        Device device = deviceService.getDeviceByPatientId(id);
        if (device != null) {
            // Delete box activities
            List<BoxActivity> activities = boxActivityService.getActivitiesByDeviceId(device.getDeviceId());
            if (activities != null) {
                activities.forEach(activity -> boxActivityService.deleteActivity(activity.getActivityId()));
            }
            
            // Delete device
            deviceService.deleteDevice(device.getDeviceId());
        }

        // Delete medicine schedules and their reminders
        List<MedicineSchedule> schedules = medicineScheduleRepository.findByPatientId(id);
        if (schedules != null) {
            for (MedicineSchedule schedule : schedules) {
                // Delete associated reminders
                List<Reminder> reminders = reminderService.getRemindersByScheduleId(schedule.getId());
                if (reminders != null) {
                    reminders.forEach(reminder -> reminderService.deleteReminder(reminder.getReminderId()));
                }
                
                // Delete the schedule
                medicineScheduleRepository.delete(schedule);
            }
        }

        // Delete medicines
        List<Medicine> medicines = medicineRepository.findByPatientId(id);
        if (medicines != null) {
            medicines.forEach(medicine -> medicineRepository.delete(medicine));
        }

        // Delete reports
        List<Report> reports = reportService.getReportsByPatientId(id);
        if (reports != null) {
            reports.forEach(report -> reportService.deleteReport(report.getReportId()));
        }

        // Finally, delete the patient
        patientRepository.delete(patient);
        log.info("Successfully deleted patient with ID: {}", id);
    }

    @Override
    @Transactional
    public Patient createPatient(PatientCreationRequest request) {
        log.debug("Creating new patient with {} medicine schedules", request.getMedicineSchedules().size());
        
        // Get current user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Create and save patient
        Patient patient = new Patient();
        patient.setName(request.getName());
        patient.setDeviceId(request.getDeviceId());
        patient.setUser(currentUser);
        patient = patientRepository.save(patient);

        // Process each medicine schedule
        for (PatientCreationRequest.MedicineScheduleDTO scheduleDTO : request.getMedicineSchedules()) {
            // Create and save medicine
            Medicine medicine = new Medicine();
            medicine.setMedicineName(scheduleDTO.getMedicineName());
            medicine.setDosage(scheduleDTO.getDosage());
            medicine.setPatient(patient);
            medicine.setStartDate(LocalDate.now());
            medicine.setDosage(scheduleDTO.getDosage());
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