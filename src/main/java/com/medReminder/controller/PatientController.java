package com.medReminder.controller;

import com.medReminder.entity.Patient;
import com.medReminder.entity.Medicine;
import com.medReminder.service.PatientService;
import com.medReminder.service.UserService;
import com.medReminder.entity.User; 
import com.medReminder.dto.PatientCreationRequest;
import com.medReminder.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class PatientController {
    private final PatientService patientService;
    private final MedicineRepository medicineRepository;
    private final UserService userService; 

    @PostMapping("/create")
    public ResponseEntity<Patient> createPatientFromRequest(@Valid @RequestBody PatientCreationRequest request) {
        System.out.println("=== CALLED /create ENDPOINT ===");
        return ResponseEntity.ok(patientService.createPatient(request));
    }

    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
         System.out.println("=== PATIENT CREATION DEBUG ===");
    
    // Get authentication from SecurityContext
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    System.out.println("Authentication: " + (auth != null ? "EXISTS" : "NULL"));
    
    if (auth != null) {
        System.out.println("Principal: " + auth.getPrincipal());
        System.out.println("Is authenticated: " + auth.isAuthenticated());
    }
        return ResponseEntity.ok(patientService.savePatient(patient));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        patient.setId(id);
        return ResponseEntity.ok(patientService.savePatient(patient));
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Patient> updatePatientWithSchedules(@PathVariable Long id, @Valid @RequestBody PatientCreationRequest request) {
        return ResponseEntity.ok(patientService.updatePatient(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok().build();
    }
}