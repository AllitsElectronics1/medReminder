package com.medReminder.repository;

import com.medReminder.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByScheduleId(Long scheduleId);
} 