package com.example.degree.service;


import com.example.degree.entities.DocumentTable;
import com.example.degree.repositories.DocumentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class DocumentService {

    private final DocumentRepo documentRepository;

    @Autowired
    public DocumentService(DocumentRepo documentRepository) {
        this.documentRepository = documentRepository;
    }

    public DocumentTable createDocument(DocumentTable document) {
        document.setCreatedDate(LocalDate.now());


        // You may add other business logic or validations here before saving the document
        return documentRepository.save(document);
    }

    public DocumentTable updateDocument(DocumentTable document) {
        // You may add other business logic or validations here before updating the document
        return documentRepository.save(document);
    }

    public DocumentTable getDocumentById(Long id) {
        return documentRepository.findById(id).orElse(null);
    }

    // You can add more methods as needed for your application

}