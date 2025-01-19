package fitness_app_be.fitness_app.persistence.jpa_repositories;

import fitness_app_be.fitness_app.persistence.entity.DietEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JpaDietRepository extends JpaRepository<DietEntity, Long> {

    @Query("SELECT d FROM DietEntity d JOIN FETCH d.meals WHERE d.userId = :userId")
    Optional<DietEntity> findDietEntityByUserId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT d FROM DietEntity d LEFT JOIN FETCH d.meals WHERE d.id = :id")
    Optional<DietEntity> findByIdWithMeals(@Param("id") Long id);

}
