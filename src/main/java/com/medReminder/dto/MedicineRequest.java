package com.medReminder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.List;

@Data
public class MedicineRequest {
    // Optional - if not provided, will use current user's first patient
    private Long patientId;
    
    @NotBlank(message = "Medicine name is required")
    private String medicineName;
    
    @NotBlank(message = "Dosage is required")
    private String dosage;
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    // Schedule information
    private List<DayOfWeek> daysOfWeek;
    private LocalTime time;
    private Integer reminderMinutesBefore = 30;
    
    // Device information (optional)
    private String deviceId;
} 