package fitness_app_be.fitness_app.persistence.jpa_repositories;

import fitness_app_be.fitness_app.persistence.entity.WorkoutStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaWorkoutStatusRepository extends JpaRepository<WorkoutStatusEntity, Long> {
    Optional<WorkoutStatusEntity> findByWorkoutPlanIdAndWorkoutId(Long workoutPlanId, Long workoutId);

    List<WorkoutStatusEntity> findByWeekNumber(Integer weekNumber);

    List<WorkoutStatusEntity> findByWorkoutPlanId(Long workoutPlanId);
}
