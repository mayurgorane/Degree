package com.example.degree.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Degree {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long degreeId;

    private LocalDate createdDate;
    private LocalDate endDate;
    private LocalDate issueDate;
    private LocalDate modifiedDate;
    private LocalDate startDate;

    @ManyToOne
    @JoinColumn(name = "institute_config_table_id")
    @JsonIgnore
    private ConfigTable configTable;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Users user;


    @OneToMany(mappedBy = "degree", cascade = CascadeType.ALL )
    @JsonIgnore
    private List<Notes> notes;

    @OneToOne(mappedBy = "degree", cascade = CascadeType.ALL )
    @JsonIgnore
    private DocumentTable document;

    public void setDegreeId(Long degreeId) {
        this.degreeId = degreeId;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public void setModifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public void setConfigTable(ConfigTable configTable) {
        this.configTable = configTable;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

  public void setNotes(List<Notes> notes) {
        this.notes = notes;
    }

    public void setDocument(DocumentTable document) {
        this.document = document;
    }

    public Long getDegreeId() {
        return degreeId;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalDate getModifiedDate() {
        return modifiedDate;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public ConfigTable getConfigTable() {
        return configTable;
    }

    public Users getUser() {
        return user;
    }

   public List<Notes> getNotes() {
        return notes;
    }

    public DocumentTable getDocument() {
        return document;
    }
}
