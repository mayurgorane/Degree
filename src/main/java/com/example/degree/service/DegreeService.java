package com.example.degree.service;

import com.example.degree.entities.ConfigTable;
import com.example.degree.entities.Degree;
import com.example.degree.repositories.ConfigTableRepo;
import com.example.degree.repositories.DegreeRepo;
import com.example.degree.repositories.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Service
public class DegreeService {

    @Autowired
    private DegreeRepo degreeRepository;

    @Autowired
    private ConfigTableRepo configTableRepository;

    @Autowired
    private UsersRepo usersRepository;

    public Degree createDegree(Long userId, Long masterId, String value, Degree degree) {
        Optional<ConfigTable> configTable = configTableRepository.findByMasterType_IdAndValue(masterId, value);

        if (configTable.isPresent()) {

            degree.setUser(usersRepository.findById(userId).orElse(null));
            degree.setConfigTable(configTable.get());

            if (degree.getCreatedDate() == null) {
                degree.setCreatedDate(LocalDate.now());
            }
            if (degree.getModifiedDate() == null) {
                degree.setModifiedDate(LocalDate.now());
            }

            return degreeRepository.save(degree);
        } else {
            return null;
        }
    }

    public Degree updateDegree(Long degreeId, Long masterId, String value, Degree updatedDegree) {
        Optional<Degree> optionalDegree = degreeRepository.findById(degreeId);
        if (optionalDegree.isPresent()) {
            Degree existingDegree = optionalDegree.get();


            LocalDate createdDate = existingDegree.getCreatedDate();
            updatedDegree.setCreatedDate(createdDate);


            updatedDegree.setModifiedDate(LocalDate.now());


            Optional<ConfigTable> configTable = configTableRepository.findByMasterType_IdAndValue(masterId, value);
            configTable.ifPresent(conf -> updatedDegree.setConfigTable(conf));


            updatedDegree.setDegreeId(degreeId);
            updatedDegree.setUser(existingDegree.getUser());


            return degreeRepository.save(updatedDegree);
        } else {
            return null;
        }
    }

    public List<Degree> getAllDegrees() {
        return degreeRepository.findAll();
    }

    public Optional<Degree> getDegreeById(Long id) {
        return degreeRepository.findById(id);
    }

    public Page<Degree> getDegreesByUserId(Long userId, Pageable pageable) {
        return degreeRepository.findByUserId(userId, pageable);
    }

    public List<Degree> getAllDegreesByUserId(Long userId) {
        return degreeRepository.findByUserId(userId);
    }

    public String getConfigTableValueByDegreeId(Long degreeId) {
        Optional<Degree> degree = degreeRepository.findById(degreeId);
        return degree.map(value -> value.getConfigTable().getValue()).orElse(null);
    }

    public String getMasterTypeTypeByDegreeId(Long degreeId) {
        Optional<Degree> degree = degreeRepository.findById(degreeId);
        return degree.map(value -> value.getConfigTable().getMasterType().getType()).orElse(null);
    }

    public boolean deleteDegreeById(Long id) {
        Optional<Degree> degreeOptional = degreeRepository.findById(id);
        if (degreeOptional.isPresent()) {
            degreeRepository.delete(degreeOptional.get());
            return true;
        }
        return false;
    }


}