package com.enterprise.repository;

import com.enterprise.entity.FileTransfer;
import com.enterprise.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FileTransferRepository extends JpaRepository<FileTransfer, Long> {
    
    // 查找用户发送的文件传输记录
    List<FileTransfer> findBySenderOrderByCreatedAtDesc(User sender);
    
    // 查找用户接收的文件传输记录
    List<FileTransfer> findByReceiverOrderByCreatedAtDesc(User receiver);
    
    // 查找用户相关的所有文件传输记录（发送或接收）
    @Query("SELECT ft FROM FileTransfer ft WHERE ft.sender = :user OR ft.receiver = :user ORDER BY ft.createdAt DESC")
    List<FileTransfer> findByUserRelated(@Param("user") User user);
    
    // 查找特定状态的文件传输记录
    List<FileTransfer> findByStatusOrderByCreatedAtDesc(String status);
    
    // 查找用户接收的未读文件传输记录
    @Query("SELECT ft FROM FileTransfer ft WHERE ft.receiver = :receiver AND ft.status = 'SENT' ORDER BY ft.sentAt DESC")
    List<FileTransfer> findUnreadByReceiver(@Param("receiver") User receiver);
    
    // 查找过期的文件传输记录
    @Query("SELECT ft FROM FileTransfer ft WHERE ft.expiresAt < :now AND ft.status IN ('PENDING', 'SENT')")
    List<FileTransfer> findExpiredTransfers(@Param("now") LocalDateTime now);
    
    // 查找两个用户之间的文件传输记录
    @Query("SELECT ft FROM FileTransfer ft WHERE (ft.sender = :user1 AND ft.receiver = :user2) OR (ft.sender = :user2 AND ft.receiver = :user1) ORDER BY ft.createdAt DESC")
    List<FileTransfer> findBetweenUsers(@Param("user1") User user1, @Param("user2") User user2);
    
    // 统计用户发送的文件数量
    long countBySender(User sender);
    
    // 统计用户接收的文件数量
    long countByReceiver(User receiver);
}

