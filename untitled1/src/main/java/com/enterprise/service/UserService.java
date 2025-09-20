package com.enterprise.service;

import com.enterprise.entity.User;
import java.util.List;

public interface UserService {
    void register(User user);
    void updateUser(User user);
    void deleteUser(Long id);
    User getUserById(Long id);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    boolean authenticate(String username, String password);
}