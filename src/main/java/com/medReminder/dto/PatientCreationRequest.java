package com.medReminder.dto;

import lombok.Data;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.List;

@Data
public class PatientCreationRequest {
    private String name;
    private String deviceId;
    private List<MedicineScheduleDTO> medicineSchedules;

    @Data
    public static class MedicineScheduleDTO {
        private String medicineName;
        private String dosage;
        private List<DayOfWeek> daysOfWeek;
        private LocalTime time;
        private boolean reminder;
        private Integer reminderMinutesBefore = 30;
    }
} 