package com.medReminder.service;

import com.medReminder.entity.Medicine;
import java.util.List;

public interface MedicineService {
    Medicine createMedicine(Medicine medicine);
    Medicine getMedicineById(Long id);
    List<Medicine> getMedicinesByPatientId(Long patientId);
    Medicine updateMedicine(Long id, Medicine medicine);
    void deleteMedicine(Long id);
} 