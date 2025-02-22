package fitness_app_be.fitness_app.persistence.jpa_repositories;

import fitness_app_be.fitness_app.persistence.entity.WorkoutPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaWorkoutPlanRepository extends JpaRepository<WorkoutPlanEntity, Long> {

    boolean existsByUserId(long userId);

    Optional<WorkoutPlanEntity> findByUserId(Long userId);
}
