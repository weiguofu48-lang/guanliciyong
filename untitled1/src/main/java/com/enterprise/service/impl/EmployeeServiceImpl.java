package com.enterprise.service.impl;

import com.enterprise.entity.Employee;
import com.enterprise.repository.EmployeeRepository;
import com.enterprise.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        logger.info("Retrieved {} employees from the database.", employees.size());
        return employees;
    }

    @Override
    public long count() {
        return employeeRepository.count();
    }

    @Override
    public Employee save(Employee employee) {
        logger.info("Saving employee with ID: {}", employee.getEmployeeId());
        return employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteEmployee(Long id) {
        logger.info("Attempting to delete employee with ID: {}", id);
        try {
            // 检查员工是否存在
            Employee employee = employeeRepository.findById(id).orElse(null);
            if (employee == null) {
                throw new RuntimeException("员工不存在");
            }
            
            employeeRepository.deleteById(id);
            logger.info("Successfully deleted employee with ID: {}", id);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            logger.error("Cannot delete employee with ID: {} due to foreign key constraints. Error: {}", id, e.getMessage());
            throw new RuntimeException("无法删除员工，该员工还有相关记录，请先删除相关记录");
        } catch (Exception e) {
            logger.error("Failed to delete employee with ID: {}. Error: {}", id, e.getMessage());
            throw new RuntimeException("删除员工失败: " + e.getMessage());
        }
    }
}
