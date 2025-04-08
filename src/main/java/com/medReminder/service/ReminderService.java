package com.medReminder.service;

import com.medReminder.entity.Reminder;
import java.util.List;

public interface ReminderService {
    Reminder createReminder(Reminder reminder);
    Reminder getReminderById(Long id);
    List<Reminder> getRemindersByScheduleId(Long scheduleId);
    Reminder updateReminder(Long id, Reminder reminder);
    void deleteReminder(Long id);
} 