package com.medReminder.service.impl;

import com.medReminder.entity.Reminder;
import com.medReminder.repository.ReminderRepository;
import com.medReminder.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReminderServiceImpl implements ReminderService {
    
    private final ReminderRepository reminderRepository;

    @Override
    public Reminder createReminder(Reminder reminder) {
        return reminderRepository.save(reminder);
    }

    @Override
    public Reminder getReminderById(Long id) {
        return reminderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reminder not found with id: " + id));
    }

    @Override
    public List<Reminder> getRemindersByScheduleId(Long scheduleId) {
        return reminderRepository.findByScheduleId(scheduleId);
    }

    @Override
    public Reminder updateReminder(Long id, Reminder reminder) {
        Reminder existingReminder = getReminderById(id);
        existingReminder.setStatus(reminder.getStatus());
        existingReminder.setReminderTime(reminder.getReminderTime());
        return reminderRepository.save(existingReminder);
    }

    @Override
    public void deleteReminder(Long id) {
        reminderRepository.deleteById(id);
    }
} 