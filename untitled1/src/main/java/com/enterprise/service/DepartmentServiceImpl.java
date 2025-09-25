package com.enterprise.service;

import com.enterprise.entity.Department;
import com.enterprise.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public List<Department> findAll() {
        List<Department> departments = departmentRepository.findAll();
        logger.info("Retrieved {} departments from the database.", departments.size());
        return departments;
    }

    @Override
    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public void deleteById(Long id) {
        logger.info("Attempting to delete department with ID: {}", id);
        try {
            // 检查是否有员工引用该部门
            Department department = departmentRepository.findById(id).orElse(null);
            if (department == null) {
                throw new RuntimeException("部门不存在");
            }
            
            departmentRepository.deleteById(id);
            logger.info("Successfully deleted department with ID: {}", id);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            logger.error("Cannot delete department with ID: {} due to foreign key constraints. Error: {}", id, e.getMessage());
            throw new RuntimeException("无法删除部门，该部门下还有员工，请先删除相关员工");
        } catch (Exception e) {
            logger.error("Failed to delete department with ID: {}. Error: {}", id, e.getMessage());
            throw new RuntimeException("删除部门失败: " + e.getMessage());
        }
    }

    @Override
    public Department findById(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    @Override
    public long count() {
        return departmentRepository.count();
    }
}
