package fitness_app_be.fitness_app.persistence.repositories.impl;


import fitness_app_be.fitness_app.domain.Workout;
import fitness_app_be.fitness_app.persistence.jpaRepositories.JpaWorkoutRepository;
import fitness_app_be.fitness_app.persistence.mapper.WorkoutEntityMapper;
import fitness_app_be.fitness_app.persistence.repositories.WorkoutRepository;
import fitness_app_be.fitness_app.persistence.entity.WorkoutEntity;
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
                .map(workoutMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Workout create(Workout workout) {
        WorkoutEntity savedEntity = jpaWorkoutRepository.save(workoutMapper.toEntity(workout));
        return workoutMapper.toDomain(savedEntity);
    }

    @Override
    public Workout update(Workout workout) {
        if (!exists(workout.getId())) {
            throw new IllegalArgumentException("Workout not found");
        }
        WorkoutEntity updatedEntity = jpaWorkoutRepository.save(workoutMapper.toEntity(workout));
        return workoutMapper.toDomain(updatedEntity);
    }

    @Override
    public void delete(long workoutId) {
        jpaWorkoutRepository.deleteById(workoutId);
    }

    @Override
    public Optional<Workout> getWorkoutById(long workoutId) {
        return jpaWorkoutRepository.findById(workoutId).map(workoutMapper::toDomain);
    }


    @Override
    public List<Workout> findByNameContainingIgnoreCase(String name) {
        return jpaWorkoutRepository.findByNameContainingIgnoreCase(name).stream()
                .map(workoutMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Workout> findByExercises(String exercise) {
        return jpaWorkoutRepository.findByExercises_NameContaining(exercise).stream()
                .map(workoutMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Workout> findByDescriptionContainingIgnoreCase(String keyword) {
        return jpaWorkoutRepository.findByDescriptionContainingIgnoreCase(keyword).stream()
                .map(workoutMapper::toDomain)
                .collect(Collectors.toList());
    }
}

