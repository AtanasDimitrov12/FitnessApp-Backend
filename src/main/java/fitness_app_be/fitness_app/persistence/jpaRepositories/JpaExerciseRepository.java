package fitness_app_be.fitness_app.persistence.jpaRepositories;

import fitness_app_be.fitness_app.domain.Exercise;
import fitness_app_be.fitness_app.persistence.entity.ExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaExerciseRepository  extends JpaRepository<ExerciseEntity, Long> {
    Optional<ExerciseEntity> findByName(String name);
}
