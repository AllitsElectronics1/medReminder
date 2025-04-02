package com.medReminder.controller;

import com.medReminder.entity.MedicineSchedule;
import com.medReminder.service.MedicineScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/medicine-schedules")
@RequiredArgsConstructor
@Tag(name = "Medicine Schedule", description = "Medicine Schedule management APIs")
public class MedicineScheduleController {
    private final MedicineScheduleService scheduleService;

    @Operation(
        summary = "Create a new medicine schedule",
        description = "Creates a new schedule for medicine intake with specific dosage time and reminder settings"
    )
    @ApiResponse(responseCode = "200", description = "Schedule created successfully")
    @PostMapping
    public ResponseEntity<MedicineSchedule> createSchedule(
            @Parameter(description = "Schedule details") @RequestBody MedicineSchedule schedule) {
        return ResponseEntity.ok(scheduleService.createSchedule(schedule));
    }

    @Operation(
        summary = "Get schedule by ID",
        description = "Retrieves a specific medicine schedule by its ID"
    )
    @ApiResponse(responseCode = "200", description = "Schedule found")
    @ApiResponse(responseCode = "404", description = "Schedule not found")
    @GetMapping("/{id}")
    public ResponseEntity<MedicineSchedule> getScheduleById(
            @Parameter(description = "Schedule ID") @PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.getScheduleById(id));
    }

    @Operation(
        summary = "Get schedules by medicine ID",
        description = "Retrieves all schedules for a specific medicine"
    )
    @ApiResponse(responseCode = "200", description = "List of schedules retrieved")
    @GetMapping("/medicine/{medicineId}")
    public ResponseEntity<List<MedicineSchedule>> getSchedulesByMedicineId(
            @Parameter(description = "Medicine ID") @PathVariable Long medicineId) {
        return ResponseEntity.ok(scheduleService.getSchedulesByMedicineId(medicineId));
    }

    @Operation(
        summary = "Get active schedules by time",
        description = "Retrieves all active schedules for a specific time"
    )
    @ApiResponse(responseCode = "200", description = "List of active schedules retrieved")
    @GetMapping("/time")
    public ResponseEntity<List<MedicineSchedule>> getActiveSchedulesByTime(
            @Parameter(description = "Time to check for schedules (HH:mm)") @RequestParam LocalTime time) {
        return ResponseEntity.ok(scheduleService.getActiveSchedulesByTime(time));
    }

    @Operation(
        summary = "Update schedule",
        description = "Updates an existing medicine schedule"
    )
    @ApiResponse(responseCode = "200", description = "Schedule updated successfully")
    @ApiResponse(responseCode = "404", description = "Schedule not found")
    @PutMapping("/{id}")
    public ResponseEntity<MedicineSchedule> updateSchedule(
            @Parameter(description = "Schedule ID") @PathVariable Long id,
            @Parameter(description = "Updated schedule details") @RequestBody MedicineSchedule schedule) {
        return ResponseEntity.ok(scheduleService.updateSchedule(id, schedule));
    }

    @Operation(
        summary = "Update schedule status",
        description = "Activates or deactivates a medicine schedule"
    )
    @ApiResponse(responseCode = "200", description = "Schedule status updated successfully")
    @ApiResponse(responseCode = "404", description = "Schedule not found")
    @PutMapping("/{id}/status")
    public ResponseEntity<MedicineSchedule> updateScheduleStatus(
            @Parameter(description = "Schedule ID") @PathVariable Long id,
            @Parameter(description = "Active status (true/false)") @RequestParam boolean isActive) {
        return ResponseEntity.ok(scheduleService.updateScheduleStatus(id, isActive));
    }

    @Operation(
        summary = "Delete schedule",
        description = "Deletes a medicine schedule"
    )
    @ApiResponse(responseCode = "200", description = "Schedule deleted successfully")
    @ApiResponse(responseCode = "404", description = "Schedule not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(
            @Parameter(description = "Schedule ID") @PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.ok().build();
    }
} 