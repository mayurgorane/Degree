package com.example.degree.controller;
import org.springframework.transaction.annotation.Transactional;
import com.example.degree.entities.Degree;
import com.example.degree.entities.Notes;
import com.example.degree.repositories.DegreeRepo;
import com.example.degree.repositories.NotesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/degreeId/{degreeId}/notes")
public class NotesController {

    @Autowired
    private NotesRepo notesRepository;

    @Autowired
    private DegreeRepo degreeRepository;

    @PostMapping()
    public List<Notes> addOrUpdateNotes(@PathVariable Long degreeId, @RequestBody List<Notes> notes) {
        Optional<Degree> degree = degreeRepository.findById(degreeId);
        if (!degree.isPresent()) {
            throw new RuntimeException("Degree not found");
        }

        List<Notes> savedNotes = new ArrayList<>();
        for (Notes note : notes) {
            Long groupId = note.getGroupId();
            Long version = note.getVersion();

            if (version != null && groupId != null) {
                // Delete the note if version and groupId are provided
                notesRepository.deleteByGroupIdAndVersion(degreeId, groupId, version);
            } else if (groupId != null) {
                // Add a new note with an incremented version if groupId is provided
                List<Notes> existingNotes = notesRepository.findTopByGroupIdAndDegreeDegreeIdOrderByVersionDesc(groupId, degreeId, PageRequest.of(0, 1));
                Notes existingNote = existingNotes.isEmpty() ? null : existingNotes.get(0);
                if (existingNote != null) {
                    note.setVersion(existingNote.getVersion() + 1);
                } else {
                    note.setVersion(1L);
                }
                note.setDegree(degree.get());
                note.setCreatedNotesDate(LocalDate.now());
                savedNotes.add(notesRepository.save(note));
            } else {
                // Add a new note with a new groupId and version 1 if only note is provided
                groupId = notesRepository.findMaxGroupIdByDegree(degreeId).orElse(0L) + 1;
                note.setGroupId(groupId);
                note.setVersion(1L);
                note.setCreatedNotesDate(LocalDate.now());
                note.setDegree(degree.get());
                savedNotes.add(notesRepository.save(note));
            }
        }

        return savedNotes;
    }

    @DeleteMapping("/group/{groupId}")
    @Transactional
    public void deleteNotesByGroupId(@PathVariable Long degreeId, @PathVariable Long groupId) {
        notesRepository.deleteByGroupIdAndDegreeDegreeId(groupId, degreeId);
    }

    @DeleteMapping("/groupId/{groupId}/versionId/{versionId}")
    @Transactional
    public void deleteNotesByGroupIdAndVersionId(@PathVariable Long degreeId, @PathVariable Long groupId, @PathVariable Long versionId) {
        notesRepository.deleteByGroupIdAndVersionAndDegreeDegreeId(groupId, versionId, degreeId);
    }

    @GetMapping("/groupId/{groupId}")
    public List<Notes> getNotesByGroupId(@PathVariable Long degreeId, @PathVariable Long groupId) {
        return notesRepository.findByGroupIdAndDegreeId(groupId, degreeId);
    }



    @GetMapping("/highestVersion")
    public List<Notes> getNotesWithHighestVersionByDegreeId(@PathVariable Long degreeId) {
        List<Notes> notes = notesRepository.findByDegreeId(degreeId);
        if (notes.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, Notes> highestVersionNotes = new HashMap<>();
        for (Notes note : notes) {
            Long groupId = note.getGroupId();
            if (!highestVersionNotes.containsKey(groupId) || highestVersionNotes.get(groupId).getVersion() < note.getVersion()) {
                highestVersionNotes.put(groupId, note);
            }
        }

        return new ArrayList<>(highestVersionNotes.values());
    }

    @GetMapping("/groupId/{groupId}/versionId/{versionId}")
    public Notes getNotesByGroupIdAndVersionId(@PathVariable Long degreeId, @PathVariable Long groupId, @PathVariable Long versionId) {
        Long previousVersionId = versionId - 1;
        return notesRepository.findByGroupIdAndVersionAndDegreeId(groupId, previousVersionId, degreeId);
    }
}