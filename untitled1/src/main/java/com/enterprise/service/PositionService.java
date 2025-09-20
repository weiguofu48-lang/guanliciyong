package com.enterprise.service;

import com.enterprise.entity.Position;

import java.util.List;

public interface PositionService {

    List<Position> findByDepartmentId(Long departmentId);

    Position save(Position position);

    void deleteById(Long id);

    Position findById(Long id);

    List<Position> findAll();
}
