package ru.bmstu.notebook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bmstu.notebook.domain.User;
import ru.bmstu.notebook.exception.NoSuchUserException;
import ru.bmstu.notebook.model.UserIn;
import ru.bmstu.notebook.model.UserOut;
import ru.bmstu.notebook.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;

    @Override
    public UserOut saveUser(UserIn data) {
        User user = new User(
                data.getName(),
                data.getSurname(),
                data.getEmail());
        User saved = repository.save(user);
        return new UserOut(
                saved.getId().toString(),
                saved.getName(),
                saved.getSurname(),
                saved.getEmail());
    }

    @Override
    public UserOut getUser(String id) {
        Optional<User> optional = repository.findById(validateId(id));
        if (!optional.isPresent()) {
            throw new NoSuchUserException(id);
        }
        User user = optional.get();
        return new UserOut(
                user.getId().toString(),
                user.getName(),
                user.getSurname(),
                user.getEmail());
    }

    @Override
    public List<UserOut> getAllUsers() {
        return repository.findAll().stream().map(user -> new UserOut(
                user.getId().toString(),
                user.getName(),
                user.getSurname(),
                user.getEmail())).collect(Collectors.toList());
    }

    @Override
    public UserOut addUser(UserIn data) {
        User user = repository.save(new User(
                data.getName(),
                data.getSurname(),
                data.getEmail()));
        return new UserOut(
                user.getId().toString(),
                user.getName(),
                user.getSurname(),
                user.getEmail());
    }

    @Override
    public UserOut updateUser(String id, UserIn data) {
        UUID uuid = validateId(id);
        Optional<User> optional = repository.findById(uuid);
        if (!optional.isPresent())
            throw new NoSuchUserException(id);
        User user = optional.get();
        user.setName(data.getName());
        user.setSurname(data.getSurname());
        user.setEmail(data.getEmail());
        User save = repository.save(user);
        return new UserOut(
                save.getId().toString(),
                save.getName(),
                save.getSurname(),
                save.getEmail());
    }

    @Override
    public void deleteUser(String id) {
        UUID uuid = validateId(id);
        if (!repository.findById(uuid).isPresent())
            throw new NoSuchUserException(id);
        repository.deleteById(uuid);
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
