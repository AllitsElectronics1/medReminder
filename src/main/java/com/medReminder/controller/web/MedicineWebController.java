package com.medReminder.controller.web;

import com.medReminder.entity.Medicine;
import com.medReminder.entity.Patient;
import com.medReminder.service.MedicineService;
import com.medReminder.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/medicines")
@RequiredArgsConstructor
@Slf4j
public class MedicineWebController {

    private final MedicineService medicineService;
    private final PatientService patientService;

    @GetMapping
    public String showMedicinesList(Model model) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Patient> patients = patientService.getPatientsForCurrentUser();
        
        if (!patients.isEmpty()) {
            Patient firstPatient = patients.get(0);
            List<Medicine> medicines = medicineService.getMedicinesByPatientId(firstPatient.getId());
            model.addAttribute("medicines", medicines);
            model.addAttribute("patient", firstPatient);
        }
        
        model.addAttribute("patients", patients);
        return "medicine/list";
    }

    @GetMapping("/add")
    public String showAddMedicineForm(Model model) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Patient> patients = patientService.getPatientsForCurrentUser();
        
        model.addAttribute("patients", patients);
        model.addAttribute("medicine", new Medicine());
        return "medicine/add";
    }

    @GetMapping("/{id}")
    public String showMedicineDetails(@PathVariable Long id, Model model) {
        Medicine medicine = medicineService.getMedicineById(id);
        model.addAttribute("medicine", medicine);
        return "medicine/details";
    }

    @GetMapping("/{id}/edit")
    public String showEditMedicineForm(@PathVariable Long id, Model model) {
        Medicine medicine = medicineService.getMedicineById(id);
        List<Patient> patients = patientService.getPatientsForCurrentUser();
        
        model.addAttribute("medicine", medicine);
        model.addAttribute("patients", patients);
        return "medicine/edit";
    }

    @GetMapping("/patient/{patientId}")
    public String showMedicinesForPatient(@PathVariable Long patientId, Model model) {
        List<Medicine> medicines = medicineService.getMedicinesByPatientId(patientId);
        Patient patient = patientService.getPatientById(patientId);
        
        model.addAttribute("medicines", medicines);
        model.addAttribute("patient", patient);
        return "medicine/list";
    }
} 