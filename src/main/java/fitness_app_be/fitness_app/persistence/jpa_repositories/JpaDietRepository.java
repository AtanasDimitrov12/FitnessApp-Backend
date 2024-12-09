package fitness_app_be.fitness_app.persistence.jpa_repositories;

import fitness_app_be.fitness_app.persistence.entity.DietEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaDietRepository extends JpaRepository<DietEntity, Long> {
    Optional<DietEntity> findDietEntitiesByUserId(Long userId);
}
