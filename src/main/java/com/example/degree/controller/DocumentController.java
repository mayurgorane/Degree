package com.example.degree.controller;




import com.example.degree.entities.DocumentTable;
import com.example.degree.exception.ResourceNotFoundException;
import com.example.degree.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.IOException;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<DocumentTable> uploadDocument(
            @RequestParam("docName") String docName,
            @RequestParam("documentImage") MultipartFile documentImage,
            @RequestParam("masterTypeId") Long masterTypeId,
            @RequestParam("configValue") String configValue,
            @RequestParam("degreeId") Long degreeId) {
        try {
            DocumentTable document = documentService.saveDocument(docName, documentImage, masterTypeId, configValue, degreeId);
            return new ResponseEntity<>(document, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/download/{documentId}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long documentId) {
        try {
            DocumentTable document = documentService.getDocumentById(documentId);

            // Set the headers for the response
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Assuming the image is JPEG

            // Return the image data as a byte array
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(document.getDocumentImage());
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}

