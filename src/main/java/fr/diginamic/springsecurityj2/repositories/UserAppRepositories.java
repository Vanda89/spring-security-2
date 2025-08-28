package fr.diginamic.springsecurityj2.repositories;

import fr.diginamic.springsecurityj2.entity.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link UserApp} entities.
 * Extends {@link JpaRepository} to provide CRUD operations.
 */
public interface UserAppRepositories extends JpaRepository<UserApp, Integer> {

    /**
     * Finds a user by their username.
     *
     * @param username the username to search for
     * @return an {@link Optional} containing the {@link UserApp} if found, or empty otherwise
     */
    Optional<UserApp> findByUsername(String username);
}
