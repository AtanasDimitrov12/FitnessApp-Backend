package fitness_app_be.fitness_app.persistence.repositories;


import fitness_app_be.fitness_app.domain.WorkoutStatus;

import java.util.List;
import java.util.Optional;

public interface WorkoutStatusRepository {
    boolean exists(long id);
    Optional<WorkoutStatus> findByWorkoutPlanIdAndWorkoutId(Long workoutPlanId, Long workoutId);
    WorkoutStatus create(WorkoutStatus workoutStatus);
    WorkoutStatus update(WorkoutStatus workoutStatus);
    List<WorkoutStatus> findAll();

    List<WorkoutStatus> findByWorkoutPlanId(Long workoutPlanId);

    void saveAll(List<WorkoutStatus> statuses);

    Long countCompletedWorkoutsByWeekRange(Long userId, int startWeek, int currentWeek);

    void deleteByWorkoutPlanId(Long workoutPlanId);


}
