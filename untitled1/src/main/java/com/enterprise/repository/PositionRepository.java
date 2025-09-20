package com.enterprise.repository;

import com.enterprise.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PositionRepository extends JpaRepository<Position, Long> {

    List<Position> findByDepartmentId(Long departmentId);
}
