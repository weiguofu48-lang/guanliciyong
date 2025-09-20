package com.enterprise.service.impl;

import com.enterprise.entity.Position;
import com.enterprise.repository.PositionRepository;
import com.enterprise.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionServiceImpl implements PositionService {

    @Autowired
    private PositionRepository positionRepository;

    @Override
    public List<Position> findAll() {
        return positionRepository.findAll();
    }

    @Override
    public Position findById(Long id) {
        return positionRepository.findById(id).orElse(null);
    }

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
}
