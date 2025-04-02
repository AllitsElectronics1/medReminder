package com.medReminder.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String deviceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicineSchedule> medicineSchedules = new ArrayList<>();

    public void setMedicineSchedules(List<MedicineSchedule> schedules) {
        this.medicineSchedules.clear();
        if (schedules != null) {
            schedules.forEach(schedule -> schedule.setPatient(this));
            this.medicineSchedules.addAll(schedules);
        }
    }
} 