package ru.bmstu.notebook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bmstu.notebook.domain.Note;
import ru.bmstu.notebook.domain.User;
import ru.bmstu.notebook.exception.NoSuchNoteException;
import ru.bmstu.notebook.exception.NoSuchUserException;
import ru.bmstu.notebook.model.NoteInPost;
import ru.bmstu.notebook.model.NoteInUpdate;
import ru.bmstu.notebook.model.NoteOut;
import ru.bmstu.notebook.repository.NoteRepository;
import ru.bmstu.notebook.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {
    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<NoteOut> getAllNotes(String userId) {
        Optional<User> optional = userRepository.findById(validateId(userId));
        if (!optional.isPresent())
            throw new NoSuchUserException(userId);
        User user = optional.get();
        return user.getNotes().stream().map(note -> new NoteOut(
                note.getId().toString(),
                userId,
                note.getTitle(),
                note.getNote_text(),
                note.getCreated_at(),
                note.getModified_at()
        )).collect(Collectors.toList());
    }

    @Override
    public NoteOut addNote(NoteInPost in) {
        Optional<User> optional = userRepository.findById(validateId(in.getUserId()));
        if (!optional.isPresent())
            throw new NoSuchUserException(in.getUserId());
        User user = optional.get();
        Note note = new Note(optional.get(), in.getTitle(), in.getText());
        user.getNotes().add(note);
        Note save = noteRepository.save(note);
        return new NoteOut(
                save.getId().toString(),
                user.getId().toString(),
                save.getTitle(),
                save.getNote_text(),
                save.getCreated_at(),
                save.getModified_at());
    }

    @Override
    public NoteOut updateNote(String id, NoteInUpdate in) {
        Optional<Note> optional = noteRepository.findById(validateId(id));
        if (!optional.isPresent())
            throw new NoSuchNoteException(id);

        Note note = optional.get();
        note.setNote_text(in.getText());
        note.setTitle(in.getTitle());
        note.setModified_at(System.currentTimeMillis());

        Note save = noteRepository.save(note);
        return new NoteOut(
                save.getId().toString(),
                save.getUser().getId().toString(),
                save.getTitle(),
                save.getNote_text(),
                save.getCreated_at(),
                save.getModified_at());
    }

    @Override
    public void deleteNote(String id) {
        UUID uuid = validateId(id);
        Optional<Note> optional = noteRepository.findById(uuid);
        if (!optional.isPresent())
            throw new NoSuchNoteException(id);

        noteRepository.deleteById(uuid);
    }

    private UUID validateId(String id) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException ex) {
            throw new NoSuchUserException(id);
        }
        return uuid;
    }
}
