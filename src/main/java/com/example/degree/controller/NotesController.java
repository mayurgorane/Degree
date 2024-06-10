package com.example.degree.controller;
import org.springframework.transaction.annotation.Transactional;
import com.example.degree.entities.Degree;
import com.example.degree.entities.Notes;
import com.example.degree.repositories.DegreeRepo;
import com.example.degree.repositories.NotesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/degreeId/{degreeId}/notes")
public class NotesController {

    @Autowired
    private NotesRepo notesRepository;

    @Autowired
    private DegreeRepo degreeRepository;

    @PostMapping
    public List<Notes> addNotes(@PathVariable Long degreeId, @RequestBody List<Notes> notes) {
        Optional<Degree> degree = degreeRepository.findById(degreeId);
        if (!degree.isPresent()) {
            throw new RuntimeException("Degree not found");
        }

        List<Notes> savedNotes = new ArrayList<>();
        for (Notes note : notes) {
            Long groupId = notesRepository.findMaxGroupIdByDegree(degreeId).orElse(0L) + 1;

            note.setDegree(degree.get());
            note.setVersion(1L);
            note.setGroupId(groupId);
            savedNotes.add(notesRepository.save(note));
        }

        return savedNotes;
    }

    @PutMapping("groupId/{groupId}")
    public List<Notes> updateNotes(@PathVariable Long degreeId, @PathVariable Long groupId, @RequestBody List<Notes> notes) {
        List<Notes> existingNotes = notesRepository.findTopByGroupIdAndDegreeDegreeIdOrderByVersionDesc(groupId, degreeId, PageRequest.of(0, 1));
        if (existingNotes.isEmpty()) {
            throw new RuntimeException("Group not found");
        }

        List<Notes> updatedNotes = new ArrayList<>();
        for (Notes note : notes) {
            Notes existingNote = existingNotes.get(0);

            existingNote.setNote(note.getNote());
            existingNote.setVersion(existingNote.getVersion() + 1);

            updatedNotes.add(notesRepository.save(existingNote));
        }

        return updatedNotes;
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
}