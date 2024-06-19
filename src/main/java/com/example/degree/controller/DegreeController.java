package com.example.degree.controller;

import com.example.degree.dto.DegreeInfo;
import com.example.degree.entities.Degree;
import com.example.degree.service.DegreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/degree")
public class DegreeController {

    @Autowired
    private DegreeService degreeService;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/userId/{userId}/masterId/{masterId}/value/{value}")
    public ResponseEntity<Degree> createDegree(
            @PathVariable Long userId,
            @PathVariable Long masterId,
            @PathVariable String value,
            @RequestBody Degree degree) {

        Degree savedDegree = degreeService.createDegree(userId, masterId, value, degree);
        if (savedDegree != null) {
            return ResponseEntity.ok(savedDegree);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/{id}/masterId/{masterId}/value/{value}")
    public ResponseEntity<Degree> updateDegree(
            @PathVariable Long id,
            @PathVariable Long masterId,
            @PathVariable String value,
            @RequestBody Degree updatedDegree) {

        Degree updated = degreeService.updateDegree(id, masterId, value, updatedDegree);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping
    public ResponseEntity<List<Degree>> getAllDegrees() {
        List<Degree> degrees = degreeService.getAllDegrees();
        return ResponseEntity.ok(degrees);
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/{id}")
    public ResponseEntity<Degree> getDegreeById(@PathVariable Long id) {
        return degreeService.getDegreeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getDegreesByUserId(
            @PathVariable Long userId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        if (page == null || size == null) {
            List<Degree> degrees = degreeService.getAllDegreesByUserId(userId);
            return ResponseEntity.ok(degrees);
        } else {
            Pageable pageable = PageRequest.of(page, size);
            Page<Degree> degrees = degreeService.getDegreesByUserId(userId, pageable);
            return ResponseEntity.ok(degrees);
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/{id}/degreeInfo")
    public ResponseEntity<DegreeInfo> getDegreeInfoByDegreeId(@PathVariable Long id) {
        String value = degreeService.getConfigTableValueByDegreeId(id);
        String type = degreeService.getMasterTypeTypeByDegreeId(id);

        if (value != null && type != null) {
            DegreeInfo degreeInfo = new DegreeInfo();
            degreeInfo.setValue(value);
            degreeInfo.setType(type);
            return ResponseEntity.ok(degreeInfo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDegreeById(@PathVariable Long id) {
        boolean deleted = degreeService.deleteDegreeById(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}