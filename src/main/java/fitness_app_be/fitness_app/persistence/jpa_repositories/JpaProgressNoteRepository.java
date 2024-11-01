package fitness_app_be.fitness_app.persistence.jpa_repositories;

import fitness_app_be.fitness_app.persistence.entity.ProgressNoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaProgressNoteRepository extends JpaRepository<ProgressNoteEntity, Long> {
    List<ProgressNoteEntity> findByUserId(Long userId);
}
