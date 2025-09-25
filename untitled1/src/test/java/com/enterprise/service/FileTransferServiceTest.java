package com.enterprise.service;

import com.enterprise.entity.FileTransfer;
import com.enterprise.entity.User;
import com.enterprise.repository.FileTransferRepository;
import com.enterprise.service.impl.FileTransferServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileTransferServiceTest {

    @Mock
    private FileTransferRepository fileTransferRepository;

    @Mock
    private UserService userService;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private FileTransferServiceImpl fileTransferService;

    private User sender;
    private User receiver;

    @BeforeEach
    void setUp() {
        sender = new User();
        sender.setId(1L);
        sender.setUsername("sender");
        sender.setRealName("发送者");

        receiver = new User();
        receiver.setId(2L);
        receiver.setUsername("receiver");
        receiver.setRealName("接收者");
    }

    @Test
    void testUploadFile_Success() throws Exception {
        // 准备测试数据
        when(userService.getUserById(1L)).thenReturn(sender);
        when(userService.getUserById(2L)).thenReturn(receiver);
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn("test.txt");
        when(multipartFile.getSize()).thenReturn(1024L);
        when(multipartFile.getContentType()).thenReturn("text/plain");
        when(multipartFile.getInputStream()).thenReturn(new java.io.ByteArrayInputStream("test content".getBytes()));
        when(fileTransferRepository.save(any(FileTransfer.class))).thenAnswer(invocation -> {
            FileTransfer fileTransfer = invocation.getArgument(0);
            fileTransfer.setId(1L);
            return fileTransfer;
        });

        // 执行测试
        FileTransfer result = fileTransferService.uploadFile(multipartFile, 1L, 2L, "测试文件");

        // 验证结果
        assertNotNull(result);
        assertEquals("test.txt", result.getOriginalFileName());
        assertEquals(sender, result.getSender());
        assertEquals(receiver, result.getReceiver());
        assertEquals("SENT", result.getStatus());
        assertEquals("测试文件", result.getMessage());

        // 验证方法调用
        verify(userService, times(1)).getUserById(1L);
        verify(userService, times(1)).getUserById(2L);
        verify(fileTransferRepository, times(1)).save(any(FileTransfer.class));
    }

    @Test
    void testUploadFile_EmptyFile() {
        // 准备测试数据
        when(userService.getUserById(1L)).thenReturn(sender);
        when(userService.getUserById(2L)).thenReturn(receiver);
        when(multipartFile.isEmpty()).thenReturn(true);

        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> {
            fileTransferService.uploadFile(multipartFile, 1L, 2L, "测试文件");
        });
    }

    @Test
    void testUploadFile_UserNotFound() {
        // 准备测试数据
        when(userService.getUserById(1L)).thenReturn(null);

        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> {
            fileTransferService.uploadFile(multipartFile, 1L, 2L, "测试文件");
        });
    }

    @Test
    void testGetFileTransferById() {
        // 准备测试数据
        FileTransfer fileTransfer = new FileTransfer();
        fileTransfer.setId(1L);
        fileTransfer.setOriginalFileName("test.txt");
        
        when(fileTransferRepository.findById(1L)).thenReturn(Optional.of(fileTransfer));

        // 执行测试
        FileTransfer result = fileTransferService.getFileTransferById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("test.txt", result.getOriginalFileName());
    }

    @Test
    void testGetFileTransferById_NotFound() {
        // 准备测试数据
        when(fileTransferRepository.findById(1L)).thenReturn(Optional.empty());

        // 执行测试
        FileTransfer result = fileTransferService.getFileTransferById(1L);

        // 验证结果
        assertNull(result);
    }

    @Test
    void testHasAccessToFile_Sender() {
        // 准备测试数据
        FileTransfer fileTransfer = new FileTransfer();
        fileTransfer.setSender(sender);
        fileTransfer.setReceiver(receiver);
        
        when(fileTransferRepository.findById(1L)).thenReturn(Optional.of(fileTransfer));

        // 执行测试
        boolean hasAccess = fileTransferService.hasAccessToFile(1L, 1L);

        // 验证结果
        assertTrue(hasAccess);
    }

    @Test
    void testHasAccessToFile_Receiver() {
        // 准备测试数据
        FileTransfer fileTransfer = new FileTransfer();
        fileTransfer.setSender(sender);
        fileTransfer.setReceiver(receiver);
        
        when(fileTransferRepository.findById(1L)).thenReturn(Optional.of(fileTransfer));

        // 执行测试
        boolean hasAccess = fileTransferService.hasAccessToFile(1L, 2L);

        // 验证结果
        assertTrue(hasAccess);
    }

    @Test
    void testHasAccessToFile_NoAccess() {
        // 准备测试数据
        User otherUser = new User();
        otherUser.setId(3L);
        
        FileTransfer fileTransfer = new FileTransfer();
        fileTransfer.setSender(sender);
        fileTransfer.setReceiver(receiver);
        
        when(fileTransferRepository.findById(1L)).thenReturn(Optional.of(fileTransfer));

        // 执行测试
        boolean hasAccess = fileTransferService.hasAccessToFile(1L, 3L);

        // 验证结果
        assertFalse(hasAccess);
    }
}

