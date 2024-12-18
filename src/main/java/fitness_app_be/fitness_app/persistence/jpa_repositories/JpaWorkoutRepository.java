package fitness_app_be.fitness_app.persistence.jpa_repositories;

import fitness_app_be.fitness_app.persistence.entity.WorkoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaWorkoutRepository extends JpaRepository<WorkoutEntity, Long> {

    @Query("SELECT w FROM WorkoutEntity w LEFT JOIN FETCH w.exercises")
    List<WorkoutEntity> findAllWithExercises();

    @Query("SELECT w FROM WorkoutEntity w JOIN FETCH w.exercises WHERE w.id = :workoutId")
    Optional<WorkoutEntity> findByIdWithExercises(@Param("workoutId") Long workoutId);

    List<WorkoutEntity> findByNameContainingIgnoreCase(String name);

    List<WorkoutEntity> findByExercises_NameContaining(String exerciseName);

    List<WorkoutEntity> findByDescriptionContainingIgnoreCase(String keyword);


}
