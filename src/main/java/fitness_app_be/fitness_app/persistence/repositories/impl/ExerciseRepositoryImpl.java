package fitness_app_be.fitness_app.persistence.repositories.impl;

import fitness_app_be.fitness_app.domain.Exercise;
import fitness_app_be.fitness_app.domain.MuscleGroup;
import fitness_app_be.fitness_app.persistence.entity.ExerciseEntity;
import fitness_app_be.fitness_app.persistence.jpa_repositories.JpaExerciseRepository;
import fitness_app_be.fitness_app.persistence.mapper.ExerciseEntityMapper;
import fitness_app_be.fitness_app.persistence.repositories.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ExerciseRepositoryImpl implements ExerciseRepository {

    private final JpaExerciseRepository jpaExerciseRepository;
    private final ExerciseEntityMapper exerciseEntityMapper;


    @Autowired
    public ExerciseRepositoryImpl(JpaExerciseRepository jpaExerciseRepository, ExerciseEntityMapper exerciseEntityMapper) {
        this.jpaExerciseRepository = jpaExerciseRepository;
        this.exerciseEntityMapper = exerciseEntityMapper;
    }

    @Override
    public boolean exists(long id) {
        return jpaExerciseRepository.existsById(id);
    }

    @Override
    public List<Exercise> getAll() {
        return jpaExerciseRepository.findAll().stream()
                .map(exerciseEntityMapper::toDomain)
                .toList();
    }

    @Override
    public Exercise create(Exercise exercise) {
        ExerciseEntity exerciseEntity = exerciseEntityMapper.toEntity(exercise);

        // Save the ExerciseEntity
        ExerciseEntity savedEntity = jpaExerciseRepository.save(exerciseEntity);

        // Convert and return the saved ExerciseEntity as a domain object
        return exerciseEntityMapper.toDomain(savedEntity);
    }



    @Override
    public Exercise update(Exercise exercise) {
        if (!exists(exercise.getId())) {
            throw new IllegalArgumentException("Exercise with ID " + exercise.getId() + " does not exist.");
        }
        ExerciseEntity exerciseEntity = exerciseEntityMapper.toEntity(exercise);
        ExerciseEntity updatedEntity = jpaExerciseRepository.save(exerciseEntity);
        return exerciseEntityMapper.toDomain(updatedEntity);
    }

    @Override
    public void delete(long exerciseId) {
        if (!exists(exerciseId)) {
            throw new IllegalArgumentException("Exercise with ID " + exerciseId + " does not exist.");
        }
        jpaExerciseRepository.deleteById(exerciseId);
    }

    @Override
    public Optional<Exercise> getExerciseById(long exerciseId) {
        return jpaExerciseRepository.findById(exerciseId).map(exerciseEntityMapper::toDomain);
    }

    @Override
    public Optional<Exercise> findByName(String name) {
        return jpaExerciseRepository.findByName(name).map(exerciseEntityMapper::toDomain);
    }

    @Override
    public Optional<Exercise> findById(long id){
        return jpaExerciseRepository.findById(id).map(exerciseEntityMapper::toDomain);
    }

    @Override
    public Map<MuscleGroup, Long> getCompletedExercisesPerMuscleGroup() {
        List<Object[]> results = jpaExerciseRepository.getCompletedExercisesPerMuscleGroup();

        // Transforming the result into a Map for easier access
        Map<MuscleGroup, Long> muscleGroupCounts = new EnumMap<>(MuscleGroup.class);
        for (Object[] result : results) {
            MuscleGroup muscleGroup = (MuscleGroup) result[0];
            Long count = (Long) result[1];
            muscleGroupCounts.put(muscleGroup, count);
        }
        return muscleGroupCounts;
    }
}
