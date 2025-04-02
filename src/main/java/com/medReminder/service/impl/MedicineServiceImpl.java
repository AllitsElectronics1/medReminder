package com.medReminder.service.impl;

import com.medReminder.entity.Medicine;
import com.medReminder.repository.MedicineRepository;
import com.medReminder.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {
    
    private final MedicineRepository medicineRepository;

    @Override
    public Medicine createMedicine(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    @Override
    public Medicine getMedicineById(Long id) {
        return medicineRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + id));
    }

    @Override
    public List<Medicine> getMedicinesByPatientId(Long patientId) {
        return medicineRepository.findByPatientId(patientId);
    }

    @Override
    public Medicine updateMedicine(Long id, Medicine medicine) {
        Medicine existingMedicine = getMedicineById(id);
        existingMedicine.setMedicineName(medicine.getMedicineName());
        existingMedicine.setDosage(medicine.getDosage());
        existingMedicine.setStartDate(medicine.getStartDate());
        existingMedicine.setEndDate(medicine.getEndDate());
        return medicineRepository.save(existingMedicine);
    }

    @Override
    public void deleteMedicine(Long id) {
        medicineRepository.deleteById(id);
    }
} 