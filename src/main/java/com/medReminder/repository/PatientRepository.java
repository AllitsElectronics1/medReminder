package com.medReminder.repository;

import com.medReminder.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    @Query("SELECT DISTINCT p FROM Patient p LEFT JOIN FETCH p.medicineSchedules s WHERE p.user.email = :email")
    List<Patient> findByUserEmail(@Param("email") String email);
}