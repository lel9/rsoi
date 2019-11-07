package ru.bmstu.notebook.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class UserIn {
    private String name;
    private String surname;
    private String email;

}
