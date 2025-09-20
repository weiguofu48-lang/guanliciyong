package com.enterprise.service;

import com.enterprise.entity.Position;
import com.enterprise.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// This is the incorrect file. The @Service annotation has been removed to resolve the conflict.
public class PositionServiceImpl implements PositionService {

    @Autowired
    private PositionRepository positionRepository;

    @Override
    public List<Position> findByDepartmentId(Long departmentId) {
        return positionRepository.findByDepartmentId(departmentId);
    }

    @Override
    public Position save(Position position) {
        return positionRepository.save(position);
    }

    @Override
    public void deleteById(Long id) {
        positionRepository.deleteById(id);
    }

    @Override
    public Position findById(Long id) {
        return positionRepository.findById(id).orElse(null);
    }

    @Override
    public List<Position> findAll() {
        return positionRepository.findAll();
    }
}
