package fitness_app_be.fitness_app.business;

import fitness_app_be.fitness_app.domain.Exercise;
import fitness_app_be.fitness_app.domain.MuscleGroup;

import java.util.List;
import java.util.Map;

public interface ExerciseService {
    List<Exercise> getAllExercises();
    Exercise getExerciseById(Long id);
    Exercise createExercise(Exercise exercise);
    void deleteExercise(Long id);
    Exercise updateExercise(Exercise exercise);
    Map<MuscleGroup, Long> getCompletedExercisesPerMuscleGroup();
}
