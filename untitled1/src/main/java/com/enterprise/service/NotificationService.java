package com.enterprise.service;

import com.enterprise.entity.Notification;

import java.util.List;

public interface NotificationService {

    Notification createNotification(String message);

    List<Notification> getAllNotifications();

    Notification getNotificationById(Long id);

    Notification updateNotification(Notification notification);

    void deleteNotification(Long id);
}
