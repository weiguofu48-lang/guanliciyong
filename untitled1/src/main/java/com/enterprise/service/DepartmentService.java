package com.enterprise.service;

import com.enterprise.entity.Department;

import java.util.List;

public interface DepartmentService {

    List<Department> findAll();

    Department save(Department department);

    void deleteById(Long id);

    Department findById(Long id);

    long count(); // Add count method
}
