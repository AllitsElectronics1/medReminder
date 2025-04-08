package com.medReminder.service;

import com.medReminder.dto.PatientCreationRequest;
import com.medReminder.entity.Patient;
import java.util.List;

public interface PatientService {
    Patient savePatient(Patient patient);
    Patient createPatient(PatientCreationRequest request);
    List<Patient> getAllPatients();
    Patient getPatientById(Long id);
    void deletePatient(Long id);
    List<Patient> getPatientsForCurrentUser();
    Patient updatePatient(Long id, PatientCreationRequest request);
} 