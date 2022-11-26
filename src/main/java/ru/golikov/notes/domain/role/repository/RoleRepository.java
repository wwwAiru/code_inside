package ru.golikov.notes.domain.role.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.golikov.notes.domain.role.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(String role);
}
