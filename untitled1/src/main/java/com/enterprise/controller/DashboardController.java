package com.enterprise.controller;

import com.enterprise.service.DepartmentService;
import com.enterprise.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    @Autowired
    public DashboardController(EmployeeService employeeService, DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        long employeeCount = employeeService.count();
        long departmentCount = departmentService.count();

        stats.put("employeeCount", employeeCount);
        stats.put("departmentCount", departmentCount);

        return ResponseEntity.ok(stats);
    }
}
