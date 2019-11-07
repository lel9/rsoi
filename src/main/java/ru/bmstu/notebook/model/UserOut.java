package ru.bmstu.notebook.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Getter
@Setter
public class UserOut {
    private String id;
    private String name;
    private String surname;
    private String email;
}
