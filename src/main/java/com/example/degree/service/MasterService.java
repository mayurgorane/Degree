package com.example.degree.service;

import com.example.degree.entities.MasterType;
import com.example.degree.repositories.MasterTypeRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class MasterService {

    @Autowired
    private MasterTypeRepo masterTypeRepository;

    public MasterType saveMasterType(MasterType masterType) {
        return masterTypeRepository.save(masterType);
    }

    public List<MasterType> getAllMasterTypes() {
        return masterTypeRepository.findAll();
    }

    public MasterType getMasterTypeById(Long id) {
        return masterTypeRepository.findById(id).orElse(null);
    }

    public void deleteMasterType(Long id) {
        masterTypeRepository.deleteById(id);
    }
}
