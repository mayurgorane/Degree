package com.example.degree.controller;



import com.example.degree.entities.DocumentTable;
import com.example.degree.exception.ResourceNotFoundException;
import com.example.degree.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {


    @Autowired
    private DocumentService documentService;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/upload")
    public ResponseEntity<DocumentTable> uploadDocument(
            @RequestParam("docName") String docName,
            @RequestParam("documentImage") MultipartFile documentImage,
            @RequestParam("masterTypeId") Long masterTypeId,
            @RequestParam("configValue") String configValue,
            @RequestParam("degreeId") Long degreeId,
            @RequestParam("receiveDate") LocalDate receiveDate) {
        try {
            // Extract file extension from the uploaded file
            String fileExtension = getFileExtension(documentImage.getOriginalFilename());

            DocumentTable document = documentService.saveDocument(docName, documentImage, masterTypeId, configValue, degreeId, receiveDate, fileExtension);
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

            // Determine the content type based on the file extension
            String contentType = determineContentType(document.getDocName());

            // Set the headers for the response
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));

            // Use documentNameExtension if available, otherwise use the file extension
            String fileName = document.getDocumentNameExtension() != null ?
                    "document." + document.getDocumentNameExtension() :
                    "document." + getFileExtension(document.getDocName());

            headers.setContentDispositionFormData("filename", fileName);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(document.getDocumentImage());
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/{degreeId}/update")
    public ResponseEntity<DocumentTable> updateDocument(
            @PathVariable Long degreeId,
            @RequestParam(value = "docName", required = false) String docName,
            @RequestParam(value = "documentImage", required = false) MultipartFile documentImage,
            @RequestParam(value = "masterId", required = false) Long masterId,
            @RequestParam(value = "value", required = false) String value,
            @RequestParam(value = "receiveDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate receiveDate) {
        try {
            // Extract file extension from the uploaded file
            String fileExtension = (documentImage != null) ? getFileExtension(documentImage.getOriginalFilename()) : null;

            DocumentTable updatedDocument = documentService.updateDocument(degreeId, docName, documentImage, masterId, value, receiveDate, fileExtension);
            return new ResponseEntity<>(updatedDocument, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    private String determineContentType(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "pdf":
                return "application/pdf";
            default:
                return "application/octet-stream";
        }
    }
    @GetMapping("/degree/{degreeId}")
    public ResponseEntity<DocumentTable> getDocumentDetailsByDegreeId(@PathVariable Long degreeId) {
        try {
            DocumentTable document = documentService.getDocumentDetailsByDegreeId(degreeId);
            return new ResponseEntity<>(document, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }






    }
