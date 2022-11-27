package ru.golikov.notes.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.golikov.notes.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
