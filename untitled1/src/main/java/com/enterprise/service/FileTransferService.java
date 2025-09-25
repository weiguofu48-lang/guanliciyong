package com.enterprise.service;

import com.enterprise.entity.FileTransfer;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileTransferService {
    
    /**
     * 上传文件并创建传输记录
     */
    FileTransfer uploadFile(MultipartFile file, Long senderId, Long receiverId, String message);
    
    /**
     * 下载文件
     */
    byte[] downloadFile(Long transferId, Long userId);
    
    /**
     * 获取用户发送的文件列表
     */
    List<FileTransfer> getSentFiles(Long userId);
    
    /**
     * 获取用户接收的文件列表
     */
    List<FileTransfer> getReceivedFiles(Long userId);
    
    /**
     * 获取用户相关的所有文件传输记录
     */
    List<FileTransfer> getUserFileTransfers(Long userId);
    
    /**
     * 获取两个用户之间的文件传输记录
     */
    List<FileTransfer> getFileTransfersBetweenUsers(Long userId1, Long userId2);
    
    /**
     * 标记文件为已接收
     */
    boolean markAsReceived(Long transferId, Long userId);
    
    /**
     * 删除文件传输记录
     */
    boolean deleteFileTransfer(Long transferId, Long userId);
    
    /**
     * 获取文件传输详情
     */
    FileTransfer getFileTransferById(Long transferId);
    
    /**
     * 检查用户是否有权限访问文件
     */
    boolean hasAccessToFile(Long transferId, Long userId);
    
    /**
     * 清理过期的文件传输记录
     */
    void cleanupExpiredTransfers();
    
    /**
     * 获取用户未读文件数量
     */
    long getUnreadFileCount(Long userId);
}
