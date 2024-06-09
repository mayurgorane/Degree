package com.example.degree.controller;



import com.example.degree.entities.Degree;
import com.example.degree.entities.DocumentTable;
import com.example.degree.service.DocumentService;
import com.example.degree.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/degree")
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(value = "/document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentTable> uploadDocument(@RequestPart("file") MultipartFile file, @RequestPart("document") DocumentTable document) {
        // Assuming you set the Degree object in the Document before passing it to the service
        // document.setDegree(degree);
        DocumentTable createdDocument = documentService.createDocument(document);
        return new ResponseEntity<>(createdDocument, HttpStatus.CREATED);
    }

    @GetMapping("/{degreeId}/document/{documentId}")
    public ResponseEntity<DocumentTable> getDocument(@PathVariable Long degreeId, @PathVariable Long documentId) {
        DocumentTable document = documentService.getDocumentById(documentId);
        if (document != null) {
            return new ResponseEntity<>(document, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{degreeId}/document/{documentId}")
    public ResponseEntity<DocumentTable> updateDocument(@PathVariable Long degreeId, @PathVariable Long documentId, @RequestBody DocumentTable document) {
        // Assuming you set the Degree object in the Document before passing it to the service
        // document.setDegree(degree);
        document.setId(documentId); // Ensure the ID is set for update
        DocumentTable updatedDocument = documentService.updateDocument(document);
        return new ResponseEntity<>(updatedDocument, HttpStatus.OK);
    }

    // You can add more endpoints for your requirements

}