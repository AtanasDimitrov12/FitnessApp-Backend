package fitness_app_be.fitness_app.persistence.repositories.impl;

import fitness_app_be.fitness_app.domain.WorkoutStatus;
import fitness_app_be.fitness_app.persistence.entity.WorkoutStatusEntity;
import fitness_app_be.fitness_app.persistence.jpa_repositories.JpaWorkoutStatusRepository;
import fitness_app_be.fitness_app.persistence.mapper.WorkoutStatusEntityMapper;
import fitness_app_be.fitness_app.persistence.repositories.WorkoutStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WorkoutStatusRepositoryImpl implements WorkoutStatusRepository {

    private final JpaWorkoutStatusRepository jpaWorkoutStatusRepository;
    private final WorkoutStatusEntityMapper workoutStatusEntityMapper;

    @Override
    public boolean exists(long id) {
        return jpaWorkoutStatusRepository.existsById(id);
    }

    @Override
    public Optional<WorkoutStatus> findByWorkoutPlanIdAndWorkoutId(Long workoutPlanId, Long workoutId) {
        return jpaWorkoutStatusRepository.findByWorkoutPlanIdAndWorkoutId(workoutPlanId, workoutId)
                .map(workoutStatusEntityMapper::toDomain);
    }


    @Override
    public WorkoutStatus create(WorkoutStatus workoutStatus) {
        return workoutStatusEntityMapper.toDomain(jpaWorkoutStatusRepository.save(workoutStatusEntityMapper.toEntity(workoutStatus)));
    }

    @Override
    public WorkoutStatus update(WorkoutStatus workoutStatus) {
        if (!exists(workoutStatus.getId())) {
            throw new IllegalArgumentException("Workout not found");
        }
        WorkoutStatusEntity updatedEntity = jpaWorkoutStatusRepository.save(workoutStatusEntityMapper.toEntity(workoutStatus));
        return workoutStatusEntityMapper.toDomain(updatedEntity);
    }

    @Override
    public List<WorkoutStatus> findAll() {
        return jpaWorkoutStatusRepository.findAll()
                .stream()
                .map(workoutStatusEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<WorkoutStatus> findByWorkoutPlanId(Long workoutPlanId) {
        return jpaWorkoutStatusRepository.findByWorkoutPlanId(workoutPlanId)
                .stream()
                .map(workoutStatusEntityMapper::toDomain)
                .toList();
    }

    @Override
    public void saveAll(List<WorkoutStatus> statuses) {
        List<WorkoutStatusEntity> entities = statuses.stream()
                .map(workoutStatusEntityMapper::toEntity)
                .toList();
        jpaWorkoutStatusRepository.saveAll(entities);
    }


}
