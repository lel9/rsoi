package ru.bmstu.notebook.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.bmstu.notebook.model.UserIn;
import ru.bmstu.notebook.model.UserOut;
import ru.bmstu.notebook.service.UserServiceImpl;

import java.util.List;

@RestController
class UserController {
    @Autowired
    private UserServiceImpl service;

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<UserOut> getUsersList() {
        return service.getAllUsers();
    }

    @GetMapping("/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserOut getUser(@PathVariable String id) {
        return service.getUser(id);
    }

    @PostMapping("/user/create")
    @ResponseStatus(HttpStatus.CREATED)
    public UserOut addUser(@RequestBody UserIn user) {
        return service.addUser(user);
    }

    @PutMapping("/user/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserOut updateUser(@PathVariable String id, @RequestBody UserIn user) {
        return service.updateUser(id, user);
    }

    @DeleteMapping("/user/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String id) {
        service.deleteUser(id);
    }
}
