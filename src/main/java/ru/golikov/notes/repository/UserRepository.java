package ru.golikov.notes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.golikov.notes.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findAllByEmail(String email);
}
