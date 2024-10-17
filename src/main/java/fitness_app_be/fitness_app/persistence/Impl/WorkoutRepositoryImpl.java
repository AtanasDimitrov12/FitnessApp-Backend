package fitness_app_be.fitness_app.persistence.Impl;


import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.persistence.JPARepositories.JpaWorkoutRepository;
import fitness_app_be.fitness_app.persistence.Mapper.WorkoutEntityMapper;
import fitness_app_be.fitness_app.persistence.Repositories.WorkoutRepository;
import fitness_app_be.fitness_app.persistence.Entity.WorkoutEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class WorkoutRepositoryImpl implements WorkoutRepository {

    private final JpaWorkoutRepository jpaWorkoutRepository;
    private final WorkoutEntityMapper workoutMapper;

    @Override
    public boolean exists(long id) {
        return jpaWorkoutRepository.existsById(id);
    }

    @Override
    public List<Workout> getAll() {
        return jpaWorkoutRepository.findAll().stream()
                .map(workoutMapper::entityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Workout create(Workout workout) {
        WorkoutEntity savedEntity = jpaWorkoutRepository.save(workoutMapper.domainToEntity(workout));
        return workoutMapper.entityToDomain(savedEntity);
    }

    @Override
    public Workout update(Workout workout) {
        if (!exists(workout.getId())) {
            throw new IllegalArgumentException("Workout not found");
        }
        WorkoutEntity updatedEntity = jpaWorkoutRepository.save(workoutMapper.domainToEntity(workout));
        return workoutMapper.entityToDomain(updatedEntity);
    }

    @Override
    public void delete(long workoutId) {
        jpaWorkoutRepository.deleteById(workoutId);
    }

    @Override
    public Optional<Workout> getWorkoutById(long workoutId) {
        return jpaWorkoutRepository.findById(workoutId).map(workoutMapper::entityToDomain);
    }

    @Override
    public List<Workout> getWorkoutsByTrainer(long trainerId) {
        return jpaWorkoutRepository.findByTrainerId(trainerId).stream()
                .map(workoutMapper::entityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Workout> findByNameContainingIgnoreCase(String name) {
        return jpaWorkoutRepository.findByNameContainingIgnoreCase(name).stream()
                .map(workoutMapper::entityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Workout> findByExercises(String exercise) {
        return jpaWorkoutRepository.findByExercisesContaining(exercise).stream()
                .map(workoutMapper::entityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Workout> findByDescriptionContainingIgnoreCase(String keyword) {
        return jpaWorkoutRepository.findByDescriptionContainingIgnoreCase(keyword).stream()
                .map(workoutMapper::entityToDomain)
                .collect(Collectors.toList());
    }
}

