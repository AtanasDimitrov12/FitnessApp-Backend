package fitness_app_be.fitness_app.persistence.jpa_repositories;

import fitness_app_be.fitness_app.persistence.entity.WorkoutStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaWorkoutStatusRepository extends JpaRepository<WorkoutStatusEntity, Long> {
    Optional<WorkoutStatusEntity> findByWorkoutPlanIdAndWorkoutId(Long workoutPlanId, Long workoutId);

    List<WorkoutStatusEntity> findByWeekNumber(Integer weekNumber);

    List<WorkoutStatusEntity> findByWorkoutPlanId(Long workoutPlanId);

    @Query("SELECT COUNT(ws) FROM WorkoutStatusEntity ws WHERE ws.workoutPlan.userId = :userId AND ws.isDone = true AND ws.weekNumber BETWEEN :startWeek AND :endWeek")
    Long countCompletedWorkoutsByWeekRange(@Param("userId") Long userId, @Param("startWeek") Integer startWeek, @Param("endWeek") Integer endWeek);
}
