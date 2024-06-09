package com.example.degree.controller;

import com.example.degree.entities.Degree;
import com.example.degree.service.DegreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/degree")
public class DegreeController {

    @Autowired
    private DegreeService degreeService;

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

    @GetMapping
    public ResponseEntity<List<Degree>> getAllDegrees() {
        List<Degree> degrees = degreeService.getAllDegrees();
        return ResponseEntity.ok(degrees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Degree> getDegreeById(@PathVariable Long id) {
        return degreeService.getDegreeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}