package com.enterprise.service;

import com.enterprise.entity.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> getAllEmployees();

    long count();

    Employee save(Employee employee);

    Employee getEmployeeById(Long id);

    void deleteEmployee(Long id);
}
