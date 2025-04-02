package com.medReminder.service;

import com.medReminder.entity.MedicineSchedule;
import java.time.LocalTime;
import java.util.List;

/**
 * Service interface for managing medicine schedules.
 * Handles creation, retrieval, update, and deletion of medicine intake schedules.
 */
public interface MedicineScheduleService {
    /**
     * Creates a new medicine schedule.
     *
     * @param schedule The schedule details to create
     * @return The created schedule with generated ID
     */
    MedicineSchedule createSchedule(MedicineSchedule schedule);

    /**
     * Retrieves a medicine schedule by its ID.
     *
     * @param id The ID of the schedule to retrieve
     * @return The found schedule or null if not found
     */
    MedicineSchedule getScheduleById(Long id);

    /**
     * Retrieves all schedules for a specific medicine.
     *
     * @param medicineId The ID of the medicine
     * @return List of schedules for the medicine
     */
    List<MedicineSchedule> getSchedulesByMedicineId(Long medicineId);

    /**
     * Retrieves all active schedules for a specific time.
     *
     * @param time The time to check for schedules
     * @return List of active schedules for the specified time
     */
    List<MedicineSchedule> getActiveSchedulesByTime(LocalTime time);

    /**
     * Updates an existing medicine schedule.
     *
     * @param id The ID of the schedule to update
     * @param schedule The updated schedule details
     * @return The updated schedule
     */
    MedicineSchedule updateSchedule(Long id, MedicineSchedule schedule);

    /**
     * Updates the active status of a schedule.
     *
     * @param id The ID of the schedule
     * @param isActive The new active status
     * @return The updated schedule
     */
    MedicineSchedule updateScheduleStatus(Long id, boolean isActive);

    /**
     * Deletes a medicine schedule.
     *
     * @param id The ID of the schedule to delete
     */
    void deleteSchedule(Long id);
} 