package com.example.degree.service;

import com.example.degree.entities.ConfigTable;
import com.example.degree.entities.Degree;
import com.example.degree.repositories.ConfigTableRepo;
import com.example.degree.repositories.DegreeRepo;
import com.example.degree.repositories.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            // Assuming the user is already existing in the database
            degree.setUser(usersRepository.findById(userId).orElse(null));
            degree.setConfigTable(configTable.get());

            // Set the createdDate only if it's not already set
            if (degree.getCreatedDate() == null) {
                degree.setCreatedDate(LocalDate.now());
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

            // Keep the createdDate unchanged
            LocalDate createdDate = existingDegree.getCreatedDate();
            updatedDegree.setCreatedDate(createdDate);

            // Update the modifiedDate to the current date
            updatedDegree.setModifiedDate(LocalDate.now());

            // Update the confId based on masterId and value
            Optional<ConfigTable> configTable = configTableRepository.findByMasterType_IdAndValue(masterId, value);
            configTable.ifPresent(conf -> updatedDegree.setConfigTable(conf));

            // Update other fields as needed
            updatedDegree.setDegreeId(degreeId); // Ensure the ID is set
            updatedDegree.setUser(existingDegree.getUser()); // Keep the user unchanged

            // Save the updated degree
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

    public List<Degree> getDegreesByUserId(Long userId) {
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