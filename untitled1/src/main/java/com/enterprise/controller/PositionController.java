package com.enterprise.controller;

import com.enterprise.entity.Position;
import com.enterprise.dto.PositionDTO;
import com.enterprise.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/positions")
@CrossOrigin(origins = "*")
public class PositionController {

    private static final Logger logger = LoggerFactory.getLogger(PositionController.class);

    @Autowired
    private PositionService positionService;

    @GetMapping
    public ResponseEntity<List<PositionDTO>> getAllPositions() {
        logger.info("PositionController: getAllPositions method called.");
        try {
            List<Position> positions = positionService.findAll();
            logger.info("PositionController: Found {} positions", positions.size());
            
            List<PositionDTO> positionDTOs = positions.stream()
                .map(pos -> new PositionDTO(pos.getId(), pos.getName(), pos.getDescription()))
                .collect(Collectors.toList());
            
            logger.info("PositionController: Converted to {} DTOs", positionDTOs.size());
            return ResponseEntity.ok(positionDTOs);
        } catch (Exception e) {
            logger.error("PositionController: Error getting positions", e);
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Position> getPositionById(@PathVariable Long id) {
        Position position = positionService.findById(id);
        return ResponseEntity.ok(position);
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Position>> getPositionsByDepartment(@PathVariable Long departmentId) {
        List<Position> positions = positionService.findByDepartmentId(departmentId);
        return ResponseEntity.ok(positions);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addPosition(@RequestBody Position position) {
        Map<String, Object> response = new HashMap<>();
        try {
            logger.info("PositionController: addPosition method called with name: {}", position.getName());
            positionService.save(position);
            response.put("success", true);
            response.put("message", "职位添加成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "添加失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePosition(@PathVariable Long id, @RequestBody Position position) {
        Map<String, Object> response = new HashMap<>();
        try {
            position.setId(id);
            positionService.save(position);
            response.put("success", true);
            response.put("message", "职位更新成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePosition(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            positionService.deleteById(id);
            response.put("success", true);
            response.put("message", "职位删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
