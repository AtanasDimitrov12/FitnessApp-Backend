package fitness_app_be.fitness_app.persistence.jpa_repositories;

import fitness_app_be.fitness_app.persistence.entity.ExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JpaExerciseRepository  extends JpaRepository<ExerciseEntity, Long> {
    Optional<ExerciseEntity> findByName(String name);

    @Query("SELECT e.muscleGroup, COUNT(ws.id) " +
            "FROM WorkoutStatusEntity ws " +
            "JOIN ws.workout w " +
            "JOIN w.exercises e " +
            "WHERE ws.isDone = true " +
            "GROUP BY e.muscleGroup")
    List<Object[]> getCompletedExercisesPerMuscleGroup();
}
