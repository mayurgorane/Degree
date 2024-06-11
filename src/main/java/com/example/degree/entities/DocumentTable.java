package com.example.degree.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class DocumentTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate createdDate;
    @OneToOne
    @JoinColumn(name = "degree_id")
    private Degree degree;

    @ManyToOne
    @JoinColumn(name = "document_config_table_id")
    private ConfigTable configTable;

    @Lob
    @Column(name = "document_image", columnDefinition="LONGBLOB")
    private byte[] documentImage;

    private String docName;

    private String createdBy;

    private LocalDate receivedDate;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public ConfigTable getConfigTable() {
        return configTable;
    }

    public Degree getDegree() {
        return degree;
    }

    public byte[] getDocumentImage() {
        return documentImage;
    }

    public String getDocName() {
        return docName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public void setConfigTable(ConfigTable configTable) {
        this.configTable = configTable;
    }

    public void setDocumentImage(byte[] documentImage) {
        this.documentImage = documentImage;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDate receivedDate) {
        this.receivedDate = receivedDate;
    }
}
