package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.ExerciseService;
import fitness_app_be.fitness_app.domain.Exercise;
import fitness_app_be.fitness_app.exception_handling.ExerciseNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;


    @Override
    public List<Exercise> getAllExercises() {
        return exerciseRepository.getAll();
    }

    @Override
    public Exercise getExerciseById(Long id) {
        return exerciseRepository.getExerciseById(id).orElseThrow(() -> new ExerciseNotFoundException(id));
    }

    @Override
    public Exercise createExercise(Exercise exercise) {
        return exerciseRepository.create(exercise);
    }

    @Override
    public void deleteExercise(Long id) {
        exerciseRepository.delete(id);
    }

    @Override
    public Exercise updateExercise(Exercise exercise) {
        return exerciseRepository.update(exercise);
    }
}
