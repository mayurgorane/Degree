package com.example.degree.controller;

import com.example.degree.entities.MasterType;
import com.example.degree.service.MasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mastertypes")
public class MasterTypeController {


    @Autowired
    private MasterService masterService;

    @PostMapping
    public ResponseEntity<MasterType> createMasterType(@RequestBody MasterType masterType) {
        MasterType savedMasterType = masterService.saveMasterType(masterType);
        return ResponseEntity.ok(savedMasterType);
    }

    @GetMapping
    public ResponseEntity<List<MasterType>> getAllMasterTypes() {
        List<MasterType> masterTypes = masterService.getAllMasterTypes();
        return ResponseEntity.ok(masterTypes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MasterType> getMasterTypeById(@PathVariable Long id) {
        MasterType masterType = masterService.getMasterTypeById(id);
        if (masterType != null) {
            return ResponseEntity.ok(masterType);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMasterType(@PathVariable Long id) {
        masterService.deleteMasterType(id);
        return ResponseEntity.noContent().build();
    }
}
