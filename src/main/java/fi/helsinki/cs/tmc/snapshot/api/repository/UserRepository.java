package fi.helsinki.cs.tmc.snapshot.api.repository;

import fi.helsinki.cs.tmc.snapshot.api.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

}
