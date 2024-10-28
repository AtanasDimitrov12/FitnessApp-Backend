package fitness_app_be.fitness_app.persistence.jpaRepositories;

import fitness_app_be.fitness_app.persistence.entity.UserDietPreferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserDietPreferenceRepository extends JpaRepository<UserDietPreferenceEntity, Long> {
    Optional<UserDietPreferenceEntity> findByUserId(long userId);
}
