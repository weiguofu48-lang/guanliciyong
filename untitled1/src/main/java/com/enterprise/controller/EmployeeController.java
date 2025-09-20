package com.enterprise.controller;

import com.enterprise.entity.Employee;
import com.enterprise.entity.Gender;
import com.enterprise.entity.EmployeeStatus;
import com.enterprise.service.DepartmentService;
import com.enterprise.service.EmployeeService;
import com.enterprise.service.PositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private PositionService positionService;

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        logger.info("EmployeeController: getAllEmployees method called.");
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addEmployee(@RequestBody Employee employee) {
        Map<String, Object> response = new HashMap<>();
        try {
            logger.info("EmployeeController: addEmployee method called for employee ID: {}", employee.getEmployeeId());
            employeeService.save(employee);
            response.put("success", true);
            response.put("message", "员工添加成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "添加失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        Map<String, Object> response = new HashMap<>();
        try {
            employee.setId(id);
            employeeService.save(employee);
            response.put("success", true);
            response.put("message", "员工更新成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteEmployee(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            employeeService.deleteEmployee(id);
            response.put("success", true);
            response.put("message", "员工删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/enums")
    public ResponseEntity<Map<String, Object>> getEnums() {
        Map<String, Object> enums = new HashMap<>();
        enums.put("genders", Gender.values());
        enums.put("statuses", EmployeeStatus.values());
        enums.put("departments", departmentService.findAll());
        enums.put("positions", positionService.findAll());
        return ResponseEntity.ok(enums);
    }
}
