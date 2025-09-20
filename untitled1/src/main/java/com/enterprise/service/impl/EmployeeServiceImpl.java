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
        employeeRepository.deleteById(id);
    }
}
