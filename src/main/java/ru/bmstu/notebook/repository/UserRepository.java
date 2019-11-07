package ru.bmstu.notebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bmstu.notebook.domain.User;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
}
