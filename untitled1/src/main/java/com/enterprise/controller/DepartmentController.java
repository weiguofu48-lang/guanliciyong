package com.enterprise.controller;

import com.enterprise.entity.Department;
import com.enterprise.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/departments")
@CrossOrigin(origins = "*")
public class DepartmentController {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        logger.info("DepartmentController: getAllDepartments method called.");
        List<Department> departments = departmentService.findAll();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        Department department = departmentService.findById(id);
        return ResponseEntity.ok(department);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addDepartment(@RequestBody Department department) {
        Map<String, Object> response = new HashMap<>();
        try {
            logger.info("DepartmentController: addDepartment method called with name: {}", department.getName());
            departmentService.save(department);
            response.put("success", true);
            response.put("message", "部门添加成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "添加失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        Map<String, Object> response = new HashMap<>();
        try {
            department.setId(id);
            departmentService.save(department);
            response.put("success", true);
            response.put("message", "部门更新成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteDepartment(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            logger.info("DepartmentController: deleteDepartment method called for ID: {}", id);
            departmentService.deleteById(id);
            response.put("success", true);
            response.put("message", "部门删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
