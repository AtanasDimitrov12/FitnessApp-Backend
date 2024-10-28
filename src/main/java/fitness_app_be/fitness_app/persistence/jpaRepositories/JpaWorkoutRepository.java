package fitness_app_be.fitness_app.persistence.jpaRepositories;

import fitness_app_be.fitness_app.persistence.entity.WorkoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaWorkoutRepository extends JpaRepository<WorkoutEntity, Long> {

    List<WorkoutEntity> findByNameContainingIgnoreCase(String name);

    List<WorkoutEntity> findByExercisesContaining(String exercise);

    List<WorkoutEntity> findByDescriptionContainingIgnoreCase(String keyword);


}
