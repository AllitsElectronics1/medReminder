package com.medReminder.service;

import com.medReminder.entity.BoxActivity;
import java.time.LocalDateTime;
import java.util.List;

public interface BoxActivityService {
    BoxActivity recordBoxActivity(BoxActivity activity);
    BoxActivity getActivityById(Long id);
    List<BoxActivity> getActivitiesByDeviceId(Long deviceId);
    List<BoxActivity> getActivitiesByDateRange(Long deviceId, LocalDateTime startDate, LocalDateTime endDate);
    BoxActivity recordBoxClose(Long id);
} 