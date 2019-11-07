package ru.bmstu.notebook.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.bmstu.notebook.model.*;
import ru.bmstu.notebook.service.NoteServiceImpl;

import java.util.List;

@RestController
public class NoteController {
    @Autowired
    private NoteServiceImpl service;

    @GetMapping("/notes/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<NoteOut> getNotesList(@PathVariable String userId) {
        return service.getAllNotes(userId);
    }

    @PostMapping("/note/create")
    @ResponseStatus(HttpStatus.CREATED)
    public NoteOut addNote(@RequestBody NoteInPost note) {
        return service.addNote(note);
    }

    @PutMapping("/note/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NoteOut updateNote(@PathVariable String id, @RequestBody NoteInUpdate note) {
        return service.updateNote(id, note);
    }

    @DeleteMapping("/note/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNote(@PathVariable String id) {
        service.deleteNote(id);
    }
}
