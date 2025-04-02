package com.medReminder.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalTime;
import java.time.DayOfWeek;

@Entity
@Data
@Table(name = "medicine_schedule")
public class MedicineSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime time;  // Time to take the medicine

    @Column(nullable = false)
    private Integer reminderMinutesBefore = 30;

    @Column(nullable = false)
    private boolean active = true;  // Using 'active' instead of 'isActive' for better JPA compatibility

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
} 