package com.medReminder.repository;

import com.medReminder.entity.Medicine;
import com.medReminder.entity.MedicineSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineScheduleRepository extends JpaRepository<MedicineSchedule, Long> {
  List<MedicineSchedule> findByPatientId(Long patientId);

  List<MedicineSchedule> findByPatientIdAndActiveOrderByTimeAsc(Long patientId, boolean active);

  boolean existsByPatientIdAndActiveAndDayOfWeekAndTime(Long patientId, boolean active, java.time.DayOfWeek dayOfWeek, java.time.LocalTime time);

  MedicineSchedule findFirstByPatientIdAndActiveAndDayOfWeekAndTimeAfterOrderByTimeAsc(Long patientId, boolean active, java.time.DayOfWeek dayOfWeek, java.time.LocalTime time);

  MedicineSchedule findByPatientIdAndActiveAndDayOfWeekAndTime(Long patientId, boolean active, java.time.DayOfWeek dayOfWeek, java.time.LocalTime time);
} 