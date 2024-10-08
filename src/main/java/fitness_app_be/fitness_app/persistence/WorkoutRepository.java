package fitness_app_be.fitness_app.persistence;

import fitness_app_be.fitness_app.domain.enums.Exercises;
import fitness_app_be.fitness_app.domain.Workout;

import java.util.List;
import java.util.Optional;

public interface WorkoutRepository {

    boolean exists(long id);

    List<Workout> getAll();

    Workout create(Workout workout);

    Workout update(Workout workout);

    void delete(long workoutId);

    Optional<Workout> getWorkoutById(long workoutId);

    List<Workout> findByNameContainingIgnoreCase(String name);

    List<Workout> findByExercises(Exercises exercise);

    List<Workout> findByDescriptionContainingIgnoreCase(String keyword);
}