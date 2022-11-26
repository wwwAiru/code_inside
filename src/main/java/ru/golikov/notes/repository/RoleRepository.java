package ru.golikov.notes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.golikov.notes.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(String role);
}
