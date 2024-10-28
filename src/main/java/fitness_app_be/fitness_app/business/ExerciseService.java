package fitness_app_be.fitness_app.business;

import fitness_app_be.fitness_app.domain.Exercise;

import java.util.List;

public interface ExerciseService {
    List<Exercise> getAllExercises();
    Exercise getExerciseById(Long id);
    Exercise createExercise(Exercise exercise);
    void deleteExercise(Long id);
    Exercise updateExercise(Exercise exercise);
}
