package ru.bmstu.notebook.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class User {

    @Id
    @GeneratedValue
    private UUID id = UUID.randomUUID();

    private String name;

    private String surname;

    private String email;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="user_id", referencedColumnName="id")
    private List<Note> notes = new ArrayList<>();

    public User(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }
}
