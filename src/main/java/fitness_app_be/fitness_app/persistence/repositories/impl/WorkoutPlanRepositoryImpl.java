package fitness_app_be.fitness_app.persistence.repositories.impl;


import fitness_app_be.fitness_app.domain.WorkoutPlan;
import fitness_app_be.fitness_app.exception_handling.WorkoutPlanNotFoundException;
import fitness_app_be.fitness_app.persistence.entity.WorkoutPlanEntity;
import fitness_app_be.fitness_app.persistence.jpa_repositories.JpaWorkoutPlanRepository;
import fitness_app_be.fitness_app.persistence.mapper.WorkoutPlanEntityMapper;
import fitness_app_be.fitness_app.persistence.repositories.WorkoutPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WorkoutPlanRepositoryImpl implements WorkoutPlanRepository {

    private final JpaWorkoutPlanRepository jpaWorkoutPlanRepository;
    private final WorkoutPlanEntityMapper workoutPlanEntityMapper;


    @Override
    public boolean exists(long id) {
        return jpaWorkoutPlanRepository.existsById(id);
    }

    @Override
    public List<WorkoutPlan> getAll() {
        return jpaWorkoutPlanRepository.findAll().stream()
                .map(workoutPlanEntityMapper::toDomain)
                .toList();
    }

    @Override
    public WorkoutPlan create(WorkoutPlan workoutPlan) {
        WorkoutPlanEntity savedEntity = jpaWorkoutPlanRepository.save(workoutPlanEntityMapper.toEntity(workoutPlan));
        return workoutPlanEntityMapper.toDomain(savedEntity);
    }

    @Override
    public WorkoutPlan update(WorkoutPlan workoutPlan) {
        if (!exists(workoutPlan.getId())) {
            throw new WorkoutPlanNotFoundException(workoutPlan.getId());
        }
        WorkoutPlanEntity updatedEntity = jpaWorkoutPlanRepository.save(workoutPlanEntityMapper.toEntity(workoutPlan));
        return workoutPlanEntityMapper.toDomain(updatedEntity);
    }

    @Override
    public void delete(long workoutPlanId) {
        jpaWorkoutPlanRepository.deleteById(workoutPlanId);
    }

    @Override
    public Optional<WorkoutPlan> getWorkoutPlanById(long workoutPlanId) {
        if (!exists(workoutPlanId)) {
            throw new WorkoutPlanNotFoundException(workoutPlanId);
        }
        return jpaWorkoutPlanRepository.findById(workoutPlanId)
                .map(workoutPlanEntityMapper::toDomain);
    }


    @Override
    public Optional<WorkoutPlan> getWorkoutPlanByUserId(long userId) {
        if (!jpaWorkoutPlanRepository.existsByUsers_Id(userId)) {
            throw new WorkoutPlanNotFoundException("Workout plan not found");
        }
        return jpaWorkoutPlanRepository.findByUsers_Id(userId)
                .map(workoutPlanEntityMapper::toDomain);
    }
}
