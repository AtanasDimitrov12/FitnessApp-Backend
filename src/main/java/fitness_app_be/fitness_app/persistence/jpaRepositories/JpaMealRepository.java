package fitness_app_be.fitness_app.persistence.jpaRepositories;

import fitness_app_be.fitness_app.persistence.entity.MealEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaMealRepository extends JpaRepository<MealEntity, Long> {
    List<MealEntity> findByNameContainingIgnoreCase(String name);
}
