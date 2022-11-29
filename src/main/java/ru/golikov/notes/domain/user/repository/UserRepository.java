package ru.golikov.notes.domain.user.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.golikov.notes.domain.user.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"roles"})
    User findByEmail(String email);

    @Override
    @EntityGraph(attributePaths = {"roles"})
    List<User> findAll();
}
