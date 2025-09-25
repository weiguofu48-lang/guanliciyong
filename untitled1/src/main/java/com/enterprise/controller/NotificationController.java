package com.enterprise.controller;

import com.enterprise.entity.Notification;
import com.enterprise.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {
        Notification notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(notification);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createNotification(@RequestBody Map<String, String> notificationData) {
        Map<String, Object> response = new HashMap<>();
        try {
            String title = notificationData.get("title");
            String content = notificationData.get("content");
            String type = notificationData.get("type");
            
            // Create a formatted message with title, content, and type
            String message = String.format("[%s] %s: %s", type, title, content);
            Notification notification = notificationService.createNotification(message);
            
            response.put("success", true);
            response.put("message", "通知创建成功");
            response.put("notification", notification);
            response.put("announcement", Map.of(
                "title", title,
                "content", content,
                "type", type,
                "timestamp", notification.getCreatedAt()
            ));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "创建失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateNotification(@PathVariable Long id, @RequestBody Map<String, String> notificationData) {
        Map<String, Object> response = new HashMap<>();
        try {
            String title = notificationData.get("title");
            String content = notificationData.get("content");
            String type = notificationData.get("type");
            
            // Create a formatted message with title, content, and type
            String message = String.format("[%s] %s: %s", type, title, content);
            
            // Get existing notification and update it
            Notification notification = notificationService.getNotificationById(id);
            if (notification == null) {
                response.put("success", false);
                response.put("message", "通知不存在");
                return ResponseEntity.badRequest().body(response);
            }
            
            notification.setMessage(message);
            notificationService.updateNotification(notification);
            
            response.put("success", true);
            response.put("message", "通知更新成功");
            response.put("notification", notification);
            response.put("announcement", Map.of(
                "title", title,
                "content", content,
                "type", type,
                "timestamp", notification.getCreatedAt()
            ));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteNotification(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            notificationService.deleteNotification(id);
            response.put("success", true);
            response.put("message", "通知删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
