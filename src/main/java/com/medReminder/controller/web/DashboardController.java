package com.medReminder.controller.web;

import com.medReminder.entity.Patient;
import com.medReminder.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DashboardController {

    private final PatientService patientService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        List<Patient> patients = patientService.getPatientsForCurrentUser();
        
        // Debug logging
        for (Patient patient : patients) {
            log.info("Patient: {} (ID: {})", patient.getName(), patient.getId());
            if (patient.getMedicineSchedules() != null) {
                log.info("  Found {} schedules for patient", patient.getMedicineSchedules().size());
                patient.getMedicineSchedules().forEach(schedule -> {
                    log.info("  Schedule: Medicine={} - {}, Time={}, Day={}",
                        schedule.getMedicine().getMedicineName(),
                        schedule.getMedicine().getDosage(),
                        schedule.getTime(),
                        schedule.getDayOfWeek());
                });
            } else {
                log.info("  No schedules found for patient");
            }
        }
        
        model.addAttribute("patients", patients);
        return "patient/patientinfo";
    }
} 