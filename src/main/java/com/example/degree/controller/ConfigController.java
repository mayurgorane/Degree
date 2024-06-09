package com.example.degree.controller;

import com.example.degree.dto.ConfigDTO;
import com.example.degree.entities.ConfigTable;
import com.example.degree.entities.MasterType;
import com.example.degree.service.ConfigService;
import com.example.degree.service.MasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mastertypes/{masterTypeId}/config")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @PostMapping
    public ResponseEntity<ConfigDTO> createConfigTable(@PathVariable Long masterTypeId, @RequestBody ConfigTable configTable) {
        // Set the MasterType for the ConfigTable
        MasterType masterType = new MasterType();
        masterType.setId(masterTypeId);
        configTable.setMasterType(masterType);

        ConfigTable savedConfigTable = configService.saveConfigTable(configTable);
        ConfigDTO configDTO = configService.convertToDTO(savedConfigTable);
        return ResponseEntity.ok(configDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConfigDTO> getConfigTableById(@PathVariable Long id) {
        ConfigDTO configDTO = configService.getConfigTableById(id);
        if (configDTO != null) {
            return ResponseEntity.ok(configDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ConfigDTO>> getAllConfigTables(@PathVariable Long masterTypeId) {
        List<ConfigDTO> configDTOs = configService.getConfigTablesByMasterTypeId(masterTypeId);
        return ResponseEntity.ok(configDTOs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConfigTable(@PathVariable Long id) {
        configService.deleteConfigTable(id);
        return ResponseEntity.noContent().build();
    }
}