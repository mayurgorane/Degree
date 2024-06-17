package com.example.degree.service;


import com.example.degree.entities.ConfigTable;
import com.example.degree.entities.Degree;
import com.example.degree.entities.DocumentTable;
import com.example.degree.entities.Users;
import com.example.degree.exception.ResourceNotFoundException;
import com.example.degree.repositories.ConfigTableRepo;
import com.example.degree.repositories.DegreeRepo;
import com.example.degree.repositories.DocumentRepo;
import com.example.degree.repositories.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class DocumentService {


    @Autowired
    private DocumentRepo documentTableRepository;

    @Autowired
    private UsersRepo userRepository;

    @Autowired
    private ConfigTableRepo configTableRepository;

    @Autowired
    private DegreeRepo degreeRepository;


    public DocumentTable saveDocument(String docName, MultipartFile documentImage, Long masterTypeId, String configValue, Long degreeId, LocalDate receiveDate, String fileExtension) throws IOException {
        Degree degree = degreeRepository.findById(degreeId)
                .orElseThrow(() -> new ResourceNotFoundException("Degree not found"));

        Users user = degree.getUser();

        if (user == null) {
            throw new ResourceNotFoundException("User not found for degree with id: " + degreeId);
        }

        ConfigTable config = configTableRepository.findByMasterTypeIdAndValue(masterTypeId, configValue)
                .orElseThrow(() -> new ResourceNotFoundException("Config not found"));

        DocumentTable documentTable = new DocumentTable();
        documentTable.setCreatedBy(user.getUserName()); // Auto-fill userName
        documentTable.setCreatedDate(LocalDate.now());
        documentTable.setDocName(docName); // Store the document name without extension
        documentTable.setDocumentNameExtension(fileExtension); // Store the file extension
        documentTable.setDocumentImage(documentImage.getBytes());
        documentTable.setConfigTable(config);
        documentTable.setDegree(degree);
        documentTable.setReceivedDate(receiveDate);

        return documentTableRepository.save(documentTable);
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    public DocumentTable getDocumentById(Long documentId) {
        return documentTableRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + documentId));
    }

    public DocumentTable getDocumentDetailsByDegreeId(Long degreeId) throws ResourceNotFoundException {
        Optional<DocumentTable> document = documentTableRepository.findByDegreeId(degreeId);
        if (document.isPresent()) {
            return document.get();
        } else {
            throw new ResourceNotFoundException("Document not found with degreeId: " + degreeId);
        }
    }


    public DocumentTable updateDocument(Long degreeId, String docName, MultipartFile documentImage, Long masterId, String value, LocalDate receiveDate, String fileExtension) throws IOException, ResourceNotFoundException {
        // Retrieve the document by degreeId
        DocumentTable document = documentTableRepository.findByDegreeId(degreeId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found for degreeId: " + degreeId));

        // Update the document properties only if new values are provided
        if (docName != null) {
            document.setDocName(docName);
        }
        if (documentImage != null) {
            document.setDocumentImage(documentImage.getBytes());
        }
        if (receiveDate != null) {
            document.setReceivedDate(receiveDate);
        }
        if (fileExtension != null) {
            document.setDocumentNameExtension(fileExtension);
        }

        // Find the ConfigTable based on masterId and value
        if (masterId != null && value != null) {
            Optional<ConfigTable> configTable = configTableRepository.findByMasterType_IdAndValue(masterId, value);
            if (configTable.isPresent()) {
                document.setConfigTable(configTable.get());
            } else {
                throw new ResourceNotFoundException("ConfigTable not found for masterId: " + masterId + " and value: " + value);
            }
        }

        // Save and return the updated document
        return documentTableRepository.save(document);
    }


}