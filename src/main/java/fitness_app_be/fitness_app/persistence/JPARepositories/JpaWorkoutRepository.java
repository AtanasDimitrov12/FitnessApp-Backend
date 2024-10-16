package fitness_app_be.fitness_app.persistence.JPARepositories;

import fitness_app_be.fitness_app.persistence.Entity.WorkoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaWorkoutRepository extends JpaRepository<WorkoutEntity, Long> {

    List<WorkoutEntity> findByNameContainingIgnoreCase(String name);

    List<WorkoutEntity> findByExercisesContaining(String exercise);

    List<WorkoutEntity> findByDescriptionContainingIgnoreCase(String keyword);
}
