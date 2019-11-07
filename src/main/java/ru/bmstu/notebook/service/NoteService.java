package ru.bmstu.notebook.service;

import org.springframework.stereotype.Service;
import ru.bmstu.notebook.model.NoteInPost;
import ru.bmstu.notebook.model.NoteInUpdate;
import ru.bmstu.notebook.model.NoteOut;

import java.util.List;

public interface NoteService {
    List<NoteOut> getAllNotes(String userId);

    NoteOut addNote(NoteInPost in);

    NoteOut updateNote(String id, NoteInUpdate in);

    void deleteNote(String id);
}
