package com.example.degree.controller;
import org.springframework.transaction.annotation.Transactional;
import com.example.degree.entities.Degree;
import com.example.degree.entities.Notes;
import com.example.degree.repositories.DegreeRepo;
import com.example.degree.repositories.NotesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;

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
    public Notes addNote(@PathVariable Long degreeId, @RequestBody String noteContent) {
        Optional<Degree> degree = degreeRepository.findById(degreeId);
        if (!degree.isPresent()) {
            throw new RuntimeException("Degree not found");
        }

        Long groupId = notesRepository.findMaxGroupIdByDegree(degreeId).orElse(0L) + 1;

        Notes note = new Notes();
        note.setDegree(degree.get());
        note.setNote(noteContent);
        note.setVersion(1L);
        note.setGroupId(groupId);

        return notesRepository.save(note);
    }

    @PutMapping("groupId/{groupId}")
    public Notes updateNote(@PathVariable Long degreeId, @PathVariable Long groupId, @RequestBody String noteContent) {
        List<Notes> existingNotes = notesRepository.findTopByGroupIdAndDegreeDegreeIdOrderByVersionDesc(groupId, degreeId, PageRequest.of(0, 1));
        if (existingNotes.isEmpty()) {
            throw new RuntimeException("Group not found");
        }

        Notes existingNote = existingNotes.get(0);

        Notes newNote = new Notes();
        newNote.setDegree(existingNote.getDegree());
        newNote.setNote(noteContent);
        newNote.setVersion(existingNote.getVersion() + 1);
        newNote.setGroupId(groupId);

        return notesRepository.save(newNote);
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
}