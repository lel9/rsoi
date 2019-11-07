package ru.bmstu.notebook.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class NoteInPost {
    private String userId;
    private String title;
    private String text;
}
