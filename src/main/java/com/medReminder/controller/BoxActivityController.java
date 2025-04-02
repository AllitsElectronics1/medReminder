package com.medReminder.controller;

import com.medReminder.entity.BoxActivity;
import com.medReminder.service.BoxActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/box-activities")
@RequiredArgsConstructor
public class BoxActivityController {
    private final BoxActivityService boxActivityService;

    @PostMapping
    public ResponseEntity<BoxActivity> recordBoxActivity(@RequestBody BoxActivity activity) {
        return ResponseEntity.ok(boxActivityService.recordBoxActivity(activity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoxActivity> getActivityById(@PathVariable Long id) {
        return ResponseEntity.ok(boxActivityService.getActivityById(id));
    }

    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<BoxActivity>> getActivitiesByDeviceId(@PathVariable Long deviceId) {
        return ResponseEntity.ok(boxActivityService.getActivitiesByDeviceId(deviceId));
    }

    @GetMapping("/device/{deviceId}/date-range")
    public ResponseEntity<List<BoxActivity>> getActivitiesByDateRange(
            @PathVariable Long deviceId,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(boxActivityService.getActivitiesByDateRange(deviceId, startDate, endDate));
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<BoxActivity> recordBoxClose(@PathVariable Long id) {
        return ResponseEntity.ok(boxActivityService.recordBoxClose(id));
    }
} 