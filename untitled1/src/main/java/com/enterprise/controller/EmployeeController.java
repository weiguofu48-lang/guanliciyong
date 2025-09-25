package com.enterprise.controller;

import com.enterprise.entity.Employee;
import com.enterprise.entity.Gender;
import com.enterprise.entity.EmployeeStatus;
import com.enterprise.entity.Department;
import com.enterprise.entity.Position;
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
    public ResponseEntity<Map<String, Object>> addEmployee(@RequestBody Map<String, Object> employeeData) {
        Map<String, Object> response = new HashMap<>();
        try {
            logger.info("EmployeeController: addEmployee method called for employee ID: {}", employeeData.get("employeeId"));
            logger.info("EmployeeController: Received data: {}", employeeData);
            
            // 创建 Employee 对象
            Employee employee = new Employee();
            employee.setName((String) employeeData.get("name"));
            employee.setEmployeeId((String) employeeData.get("employeeId"));
            employee.setGender(Gender.valueOf((String) employeeData.get("gender")));
            employee.setHireDate(java.time.LocalDate.parse((String) employeeData.get("hireDate")));
            employee.setStatus(EmployeeStatus.valueOf((String) employeeData.get("status")));
            
            logger.info("EmployeeController: Created employee object: {}", employee);
            
            // 设置部门和职位
            Long departmentId = Long.valueOf(employeeData.get("departmentId").toString());
            Long positionId = Long.valueOf(employeeData.get("positionId").toString());
            
            logger.info("EmployeeController: Looking for department ID: {} and position ID: {}", departmentId, positionId);
            
            Department department = departmentService.findById(departmentId);
            Position position = positionService.findById(positionId);
            
            if (department == null || position == null) {
                logger.error("EmployeeController: Department or position not found. Department: {}, Position: {}", department, position);
                response.put("success", false);
                response.put("message", "部门或职位不存在");
                return ResponseEntity.badRequest().body(response);
            }
            
            logger.info("EmployeeController: Found department: {} and position: {}", department.getName(), position.getName());
            
            employee.setDepartment(department);
            employee.setPosition(position);
            
            logger.info("EmployeeController: About to save employee");
            Employee savedEmployee = employeeService.save(employee);
            logger.info("EmployeeController: Employee saved successfully with ID: {}", savedEmployee.getId());
            
            response.put("success", true);
            response.put("message", "员工添加成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("EmployeeController: Error adding employee", e);
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "添加失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateEmployee(@PathVariable Long id, @RequestBody Map<String, Object> employeeData) {
        Map<String, Object> response = new HashMap<>();
        try {
            logger.info("EmployeeController: updateEmployee method called for employee ID: {}", id);
            logger.info("EmployeeController: Received data: {}", employeeData);
            
            // 获取现有员工信息
            Employee existingEmployee = employeeService.getEmployeeById(id);
            if (existingEmployee == null) {
                response.put("success", false);
                response.put("message", "员工不存在");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 更新员工信息
            existingEmployee.setName((String) employeeData.get("name"));
            existingEmployee.setEmployeeId((String) employeeData.get("employeeId"));
            existingEmployee.setGender(Gender.valueOf((String) employeeData.get("gender")));
            existingEmployee.setHireDate(java.time.LocalDate.parse((String) employeeData.get("hireDate")));
            existingEmployee.setStatus(EmployeeStatus.valueOf((String) employeeData.get("status")));
            
            // 设置部门和职位
            Long departmentId = Long.valueOf(employeeData.get("departmentId").toString());
            Long positionId = Long.valueOf(employeeData.get("positionId").toString());
            
            Department department = departmentService.findById(departmentId);
            Position position = positionService.findById(positionId);
            
            if (department == null || position == null) {
                logger.error("EmployeeController: Department or position not found. Department: {}, Position: {}", department, position);
                response.put("success", false);
                response.put("message", "部门或职位不存在");
                return ResponseEntity.badRequest().body(response);
            }
            
            existingEmployee.setDepartment(department);
            existingEmployee.setPosition(position);
            
            logger.info("EmployeeController: About to save updated employee");
            Employee savedEmployee = employeeService.save(existingEmployee);
            logger.info("EmployeeController: Employee updated successfully with ID: {}", savedEmployee.getId());
            
            response.put("success", true);
            response.put("message", "员工更新成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("EmployeeController: Error updating employee", e);
            e.printStackTrace();
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
