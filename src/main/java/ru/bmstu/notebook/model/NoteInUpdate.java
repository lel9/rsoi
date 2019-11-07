package ru.bmstu.notebook.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class NoteInUpdate {
    private String title;
    private String text;
}
