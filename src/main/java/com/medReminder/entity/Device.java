package com.medReminder.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "devices")
@Getter
@Setter
@NoArgsConstructor
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deviceId;

    @Column(nullable = false, unique = true)
    private String deviceSerialNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Column(nullable = false)
    private LocalDateTime lastSyncTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceStatus status;

    private String ipAddress;

    private String macAddress;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    private Set<BoxActivity> activities = new HashSet<>();
} 