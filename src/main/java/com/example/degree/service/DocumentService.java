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

    public DocumentTable saveDocument(String docName, MultipartFile documentImage, Long masterTypeId, String configValue, Long degreeId,LocalDate receiveDate) throws IOException {
        Degree degree = degreeRepository.findById(degreeId)
                .orElseThrow(() -> new ResourceNotFoundException("Degree not found"));

        Users user = degree.getUser();

        if (user == null) {
            throw new ResourceNotFoundException("User not found for degree with id: " + degreeId);
        }

        ConfigTable config = configTableRepository.findByMasterTypeIdAndValue(masterTypeId, configValue)
                .orElseThrow(() -> new ResourceNotFoundException("Config not found"));

       
        String fileExtension = getFileExtension(documentImage.getOriginalFilename());

        DocumentTable documentTable = new DocumentTable();
        documentTable.setCreatedBy(user.getUserName()); // Auto-fill userName
        documentTable.setCreatedDate(LocalDate.now());
        documentTable.setDocName(docName + "." + fileExtension); // Append the file extension to the docName
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


}