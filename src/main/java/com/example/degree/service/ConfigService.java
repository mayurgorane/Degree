package com.example.degree.service;

import com.example.degree.dto.ConfigDTO;
import com.example.degree.entities.ConfigTable;
import com.example.degree.repositories.ConfigTableRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConfigService {

    @Autowired
    private ConfigTableRepo configTableRepository;

    public ConfigTable saveConfigTable(ConfigTable configTable) {
        return configTableRepository.save(configTable);
    }

    public List<ConfigDTO> getAllConfigTables() {
        List<ConfigTable> configTables = configTableRepository.findAll();
        return configTables.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ConfigDTO getConfigTableById(Long id) {
        ConfigTable configTable = configTableRepository.findById(id).orElse(null);
        return configTable != null ? convertToDTO(configTable) : null;
    }

    public void deleteConfigTable(Long id) {
        configTableRepository.deleteById(id);
    }

    public List<ConfigDTO> getConfigTablesByMasterTypeId(Long masterTypeId) {
        List<ConfigTable> configTables = configTableRepository.findByMasterType_Id(masterTypeId);
        return configTables.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ConfigDTO convertToDTO(ConfigTable configTable) {
        ConfigDTO configDTO = new ConfigDTO();
        configDTO.setConfigId(configTable.getConfigId());
        configDTO.setMasterTypeId(configTable.getMasterType().getId());
        configDTO.setValue(configTable.getValue());
        return configDTO;
    }











}
