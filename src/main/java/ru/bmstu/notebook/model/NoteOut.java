package ru.bmstu.notebook.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Getter
@Setter
public class NoteOut {
    private String id;
    private String userId;
    private String title;
    private String text;
    private long created_at;
    private long modified_at;
}
