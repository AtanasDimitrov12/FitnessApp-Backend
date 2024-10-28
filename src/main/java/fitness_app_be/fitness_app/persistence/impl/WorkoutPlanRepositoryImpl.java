package fitness_app_be.fitness_app.persistence.impl;

import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.domain.WorkoutPlan;
import fitness_app_be.fitness_app.persistence.entity.UserEntity;
import fitness_app_be.fitness_app.persistence.entity.WorkoutPlanEntity;
import fitness_app_be.fitness_app.persistence.jpaRepositories.JpaUserRepository;
import fitness_app_be.fitness_app.persistence.jpaRepositories.JpaWorkoutPlanRepository;
import fitness_app_be.fitness_app.persistence.mapper.WorkoutPlanEntityMapper;
import fitness_app_be.fitness_app.persistence.repositories.WorkoutPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class WorkoutPlanRepositoryImpl implements WorkoutPlanRepository {

    private final JpaWorkoutPlanRepository jpaWorkoutPlanRepository;
    private final JpaUserRepository jpaUserRepository;
    private final WorkoutPlanEntityMapper workoutPlanEntityMapper;


    @Override
    public boolean exists(long id) {
        return jpaWorkoutPlanRepository.existsById(id);
    }

    @Override
    public List<WorkoutPlan> getAll() {
        return jpaWorkoutPlanRepository.findAll().stream()
                .map(workoutPlanEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public WorkoutPlan create(WorkoutPlan workoutPlan) {
        UserEntity user = jpaUserRepository.findById(workoutPlan.getUserId()).orElse(null);
        WorkoutPlanEntity savedEntity = jpaWorkoutPlanRepository.save(workoutPlanEntityMapper.toEntity(workoutPlan, user));
        return workoutPlanEntityMapper.toDomain(savedEntity);
    }

    @Override
    public WorkoutPlan update(WorkoutPlan workoutPlan) {
        if (!exists(workoutPlan.getId())) {
            throw new IllegalArgumentException("Workout plan not found");
        }
        UserEntity user = jpaUserRepository.findById(workoutPlan.getUserId()).orElse(null);
        WorkoutPlanEntity updatedEntity = jpaWorkoutPlanRepository.save(workoutPlanEntityMapper.toEntity(workoutPlan, user));
        return workoutPlanEntityMapper.toDomain(updatedEntity);
    }

    @Override
    public void delete(long workoutPlanId) {
        jpaWorkoutPlanRepository.deleteById(workoutPlanId);
    }

    @Override
    public Optional<WorkoutPlan> getWorkoutPlanById(long workoutPlanId) {
        if (!exists(workoutPlanId)) {
            throw new IllegalArgumentException("Workout plan not found");
        }
        return jpaWorkoutPlanRepository.findById(workoutPlanId)
                .map(workoutPlanEntityMapper::toDomain);
    }


    @Override
    public Optional<WorkoutPlan> getWorkoutPlanByUserId(long userId) {
        if (!jpaWorkoutPlanRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException("Workout plan not found");
        }
        return jpaWorkoutPlanRepository.findByUserId(userId)
                .map(workoutPlanEntityMapper::toDomain);
    }
}
