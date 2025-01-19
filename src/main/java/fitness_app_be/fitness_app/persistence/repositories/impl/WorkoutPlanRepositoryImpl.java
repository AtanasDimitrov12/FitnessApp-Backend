package fitness_app_be.fitness_app.persistence.repositories.impl;

import fitness_app_be.fitness_app.domain.WorkoutPlan;
import fitness_app_be.fitness_app.exception_handling.WorkoutPlanNotFoundException;
import fitness_app_be.fitness_app.persistence.entity.WorkoutPlanEntity;
import fitness_app_be.fitness_app.persistence.entity.WorkoutStatusEntity;
import fitness_app_be.fitness_app.persistence.jpa_repositories.JpaWorkoutPlanRepository;
import fitness_app_be.fitness_app.persistence.mapper.WorkoutPlanEntityMapper;
import fitness_app_be.fitness_app.persistence.mapper.WorkoutStatusEntityMapper;
import fitness_app_be.fitness_app.persistence.repositories.WorkoutPlanRepository;
import fitness_app_be.fitness_app.persistence.repositories.WorkoutStatusRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WorkoutPlanRepositoryImpl implements WorkoutPlanRepository {

    private final JpaWorkoutPlanRepository jpaWorkoutPlanRepository;
    private final WorkoutPlanEntityMapper workoutPlanEntityMapper;
    private final WorkoutStatusRepository workoutStatusRepository;
    private final WorkoutStatusEntityMapper workoutStatusEntityMapper;
    private final EntityManager entityManager;

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
    @Transactional
    public WorkoutPlan create(WorkoutPlan workoutPlan) {
        WorkoutPlanEntity workoutPlanEntity = workoutPlanEntityMapper.toEntity(workoutPlan);

        // Ensure all workouts in the entity are managed
        workoutPlanEntity.setWorkouts(
                workoutPlanEntity.getWorkouts().stream()
                        .map(workout -> entityManager.merge(workout))
                        .toList()
        );

        WorkoutPlanEntity savedEntity = entityManager.merge(workoutPlanEntity);
        return workoutPlanEntityMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional
    public WorkoutPlan update(WorkoutPlan workoutPlan) {
        if (!exists(workoutPlan.getId())) {
            throw new WorkoutPlanNotFoundException(workoutPlan.getId());
        }

        // Fetch the existing plan to ensure it's managed
        WorkoutPlanEntity existingPlan = entityManager.find(WorkoutPlanEntity.class, workoutPlan.getId());
        if (existingPlan == null) {
            throw new WorkoutPlanNotFoundException(workoutPlan.getId());
        }

        // Convert workoutPlan to entity but do not replace collections
        WorkoutPlanEntity updatedEntity = workoutPlanEntityMapper.toEntity(workoutPlan);

        // **Ensure Workouts List is Initialized**
        if (existingPlan.getWorkouts() == null) {
            existingPlan.setWorkouts(new ArrayList<>());
        }
        existingPlan.getWorkouts().clear();
        existingPlan.getWorkouts().addAll(
                updatedEntity.getWorkouts().stream()
                        .map(workout -> entityManager.merge(workout))
                        .toList()
        );

        // **Ensure WorkoutStatuses List is Initialized**
        if (existingPlan.getWorkoutStatuses() == null) {
            existingPlan.setWorkoutStatuses(new ArrayList<>());
        }

        // **Fetch existing workout statuses**
        List<WorkoutStatusEntity> existingStatuses = workoutStatusRepository.findByWorkoutPlanId(existingPlan.getId()).stream().map(workoutStatusEntityMapper::toEntity).toList();

        // Clear existing statuses before adding new ones
        existingPlan.getWorkoutStatuses().clear();

        // Add updated statuses while ensuring correct association
        existingStatuses.stream()
                        .map(status -> {
                            status.setWorkoutPlan(existingPlan);
                            return entityManager.merge(status);
                        }
        );

        // Save the updated entity
        WorkoutPlanEntity savedEntity = entityManager.merge(existingPlan);

        return workoutPlanEntityMapper.toDomain(savedEntity);
    }


    @Override
    public void delete(long workoutPlanId) {
        if (!exists(workoutPlanId)) {
            throw new WorkoutPlanNotFoundException(workoutPlanId);
        }
        jpaWorkoutPlanRepository.deleteById(workoutPlanId);
    }

    @Override
    public Optional<WorkoutPlan> getWorkoutPlanById(long workoutPlanId) {
        return jpaWorkoutPlanRepository.findById(workoutPlanId)
                .map(workoutPlanEntityMapper::toDomain);
    }

    @Override
    public Optional<WorkoutPlan> getWorkoutPlanByUserId(long userId) {
        return jpaWorkoutPlanRepository.findByUserId(userId)
                .map(workoutPlanEntityMapper::toDomain)
                .or(() -> {
                    throw new WorkoutPlanNotFoundException("Workout plan for user ID " + userId + " not found.");
                });
    }
}
