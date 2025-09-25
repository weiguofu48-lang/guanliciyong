package com.enterprise.service;

import com.enterprise.entity.Role;
import com.enterprise.entity.User;
import com.enterprise.repository.RoleRepository;
import com.enterprise.repository.UserRepository;
import com.enterprise.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void register(User user) {
        // 角色已经在Controller中设置，这里直接保存
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * This method was previously used with UserRole enum, which has been removed.
     * If needed in the future, it should be re-implemented using the new Role entity.
     */
    // @Override
    // public List<User> getUsersByRole(UserRole role) {
    //     // To implement this, you would add 'List<User> findByRole(UserRole role);' to UserRepository
    //     return null;
    // }

    @Override
    public boolean authenticate(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.map(user -> user.getPassword().equals(password)).orElse(false);
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }
}
