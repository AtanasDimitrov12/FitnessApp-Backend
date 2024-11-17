package fitness_app_be.fitness_app.persistence.jpa_repositories;

import fitness_app_be.fitness_app.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByUsernameAndIsActive(String username, boolean isActive);
    Optional<UserEntity> findByEmailAndIsActive(String email, boolean isActive);
    List<UserEntity> findByUsernameContainingIgnoreCase(String partialUsername);
    List<UserEntity> findByIsActive(boolean isActive);
    long countByEmail(String email);
    long countByIsActive(boolean isActive);
}

