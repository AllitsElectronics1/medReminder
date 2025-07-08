package com.medReminder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineScheduleCheckResponse {
    private boolean scheduled;
    private Long minutesUntilNextSchedule;
    private String message;
    private String label;
} 