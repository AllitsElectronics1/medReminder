package com.medReminder.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "box_activities")
@Getter
@Setter
@NoArgsConstructor
public class BoxActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long activityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Column(nullable = false)
    private LocalDateTime openTime;

    private LocalDateTime closeTime;

    @Column(nullable = false)
    private Boolean isMedicineTaken;
} 