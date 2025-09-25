package com.enterprise.controller;

import com.enterprise.entity.User;
import com.enterprise.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody Map<String, Object> userData) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = new User();
            user.setUsername((String) userData.get("username"));
            user.setPassword((String) userData.get("password"));
            user.setRealName((String) userData.get("realName"));
            user.setEmail((String) userData.get("email"));
            user.setPhone((String) userData.get("phone"));
            
            // 设置角色
            Long roleId = Long.valueOf(userData.get("roleId").toString());
            user.setRole(userService.getRoleById(roleId));
            
            userService.register(user);
            response.put("success", true);
            response.put("message", "用户注册成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "注册失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Map<String, String> loginData) {
        Map<String, Object> response = new HashMap<>();
        String username = loginData.get("username");
        String password = loginData.get("password");
        
        if (userService.authenticate(username, password)) {
            User user = userService.getUserByUsername(username);
            response.put("success", true);
            response.put("message", "登录成功");
            response.put("user", user);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "用户名或密码错误");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> userData) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 获取现有用户信息
            User existingUser = userService.getUserById(id);
            if (existingUser == null) {
                response.put("success", false);
                response.put("message", "用户不存在");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 更新用户信息
            existingUser.setUsername((String) userData.get("username"));
            existingUser.setRealName((String) userData.get("realName"));
            existingUser.setEmail((String) userData.get("email"));
            existingUser.setPhone((String) userData.get("phone"));
            
            // 只有在新密码不为空时才更新密码
            String newPassword = (String) userData.get("password");
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                existingUser.setPassword(newPassword);
            }
            
            userService.updateUser(existingUser);
            response.put("success", true);
            response.put("message", "用户更新成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            userService.deleteUser(id);
            response.put("success", true);
            response.put("message", "用户删除成功");
            return ResponseEntity.ok(response);
        } catch (DataIntegrityViolationException e) {
            response.put("success", false);
            response.put("message", "无法删除用户，该用户被其他数据引用");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
