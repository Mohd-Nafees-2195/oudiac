package com.app.oudiac.repositories;

import com.app.oudiac.models.Admin;
import com.app.oudiac.models.User;
import com.app.oudiac.models.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {
    Optional<Admin> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Admin> findAllByRole(Role role);
}
