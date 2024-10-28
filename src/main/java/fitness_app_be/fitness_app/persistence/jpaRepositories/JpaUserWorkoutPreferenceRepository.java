package fitness_app_be.fitness_app.persistence.jpaRepositories;

import fitness_app_be.fitness_app.persistence.entity.UserWorkoutPreferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserWorkoutPreferenceRepository extends JpaRepository<UserWorkoutPreferenceEntity, Long> {
    Optional<UserWorkoutPreferenceEntity> findByUserId(long userId);
}
