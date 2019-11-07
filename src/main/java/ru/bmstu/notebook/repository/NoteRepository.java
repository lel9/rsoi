package ru.bmstu.notebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bmstu.notebook.domain.Note;

import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<Note, UUID> {
}
