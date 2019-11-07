package ru.bmstu.notebook.service;

import org.springframework.stereotype.Service;
import ru.bmstu.notebook.model.UserIn;
import ru.bmstu.notebook.model.UserOut;

import java.util.List;

public interface UserService {
    UserOut saveUser(UserIn data);

    UserOut getUser(String id);

    List<UserOut> getAllUsers();

    UserOut addUser(UserIn data);

    UserOut updateUser(String id, UserIn data);

    void deleteUser(String id);
}
