package fitness_app_be.fitness_app.Persistence;

import fitness_app_be.fitness_app.Domain.Enums.Exercises;
import fitness_app_be.fitness_app.Domain.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    List<Workout> findByNameContainingIgnoreCase(String name);

    List<Workout> findByExercises(Exercises exercise);

    List<Workout> findByDescriptionContainingIgnoreCase(String keyword);
}
