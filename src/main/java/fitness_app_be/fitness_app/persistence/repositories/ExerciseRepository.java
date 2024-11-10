package fitness_app_be.fitness_app.persistence.repositories;

import fitness_app_be.fitness_app.domain.Exercise;

import java.util.List;
import java.util.Optional;

public interface ExerciseRepository {
    boolean exists(long id);

    List<Exercise> getAll();

    Exercise create(Exercise exercise);

    Exercise update(Exercise exercise);

    void delete(long exerciseId);

    Optional<Exercise> getExerciseById(long exerciseId);

    Optional<Exercise> findByName(String name);

    Optional<Exercise> findById(long id);

}
