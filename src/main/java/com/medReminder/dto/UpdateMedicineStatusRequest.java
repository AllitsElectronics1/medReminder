package com.medReminder.dto;

import lombok.Data;

@Data
public class UpdateMedicineStatusRequest {
    private String macAddress;
    private String medicineName;
    private String status;
} 