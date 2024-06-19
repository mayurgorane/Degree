package com.example.degree.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Notes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noteId;

    @ManyToOne
    @JoinColumn(name = "degree_id", referencedColumnName = "degreeId")
    private Degree degree;

    @Lob
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    private Long version;

    private Long groupId;

    private LocalDate createdNotesDate;

    public LocalDate getCreatedNotesDate() {
        return createdNotesDate;
    }

    public void setCreatedNotesDate(LocalDate createdNotesDate) {
        this.createdNotesDate = createdNotesDate;
    }

    public Long getNoteId() {
        return noteId;
    }

    public Degree getDegree() {
        return degree;
    }

    public Long getVersion() {
        return version;
    }

    public String getNote() {
        return note;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
