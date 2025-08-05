package com.medReminder.controller.web;

import com.medReminder.entity.Patient;
import com.medReminder.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;

import java.util.List;

@Controller
@RequestMapping("/patients")
@RequiredArgsConstructor
@Slf4j
public class PatientWebController {

    private final PatientService patientService;

    @GetMapping
    public String showPatientsList(Model model) {
        List<Patient> patients = patientService.getPatientsForCurrentUser();
        model.addAttribute("patients", patients);
        return "patient/list";
    }

    @GetMapping("/add")
    public String showAddPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "patient/add";
    }

    @PostMapping("/add")
    public String addPatient(@Valid @ModelAttribute("patient") Patient patient, 
                           BindingResult result, 
                           Model model) {
        if (result.hasErrors()) {
            return "patient/add";
        }
        
        try {
            patientService.savePatient(patient);
            return "redirect:/patients";
        } catch (Exception e) {
            log.error("Error creating patient: {}", e.getMessage(), e);
            model.addAttribute("error", "Failed to create patient: " + e.getMessage());
            return "patient/add";
        }
    }

    @GetMapping("/{id}")
    public String showPatientDetails(@PathVariable Long id, Model model) {
        Patient patient = patientService.getPatientById(id);
        model.addAttribute("patient", patient);
        return "patient/details";
    }

    @GetMapping("/{id}/edit")
    public String showEditPatientForm(@PathVariable Long id, Model model) {
        Patient patient = patientService.getPatientById(id);
        model.addAttribute("patient", patient);
        return "patient/edit";
    }

    @PostMapping("/{id}/edit")
    public String updatePatient(@PathVariable Long id, 
                              @Valid @ModelAttribute("patient") Patient patient, 
                              BindingResult result, 
                              Model model) {
        if (result.hasErrors()) {
            return "patient/edit";
        }
        
        try {
            patient.setId(id);
            patientService.savePatient(patient);
            return "redirect:/patients";
        } catch (Exception e) {
            log.error("Error updating patient: {}", e.getMessage(), e);
            model.addAttribute("error", "Failed to update patient: " + e.getMessage());
            return "patient/edit";
        }
    }

    @GetMapping("/{id}/medicines")
    public String showPatientMedicines(@PathVariable Long id, Model model) {
        Patient patient = patientService.getPatientById(id);
        model.addAttribute("patient", patient);
        return "patient/medicines";
    }
} 