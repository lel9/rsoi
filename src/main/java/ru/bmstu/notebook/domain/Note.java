package ru.bmstu.notebook.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "notes")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class Note {
    @Id
    @GeneratedValue
    private UUID id = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private String note_text;

    private Long created_at;

    private Long modified_at;

    public Note(User author, String title, String note_text) {
        this.user = author;
        this.title = title;
        this.note_text = note_text;
        this.created_at = System.currentTimeMillis();
        this.modified_at = created_at;
    }
}
