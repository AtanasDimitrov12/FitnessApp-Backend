package fitness_app_be.fitness_app.persistence.mapper;

import fitness_app_be.fitness_app.domain.ProgressNote;
import fitness_app_be.fitness_app.persistence.entity.ProgressNoteEntity;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class ProgressNoteEntityMapper {

    public ProgressNote toDomain(ProgressNoteEntity progressNoteEntity) {
        if (progressNoteEntity == null) {
            return null;
        }
        return new ProgressNote(
                progressNoteEntity.getId(),
                progressNoteEntity.getUserId(),
                progressNoteEntity.getWeight(),
                progressNoteEntity.getNote(),
                progressNoteEntity.getDate()
        );
    }

    public ProgressNoteEntity toEntity(ProgressNote progressNote) {
        if (progressNote == null) {
            return null;
        }

        ProgressNoteEntity progressNoteEntity = new ProgressNoteEntity();
        progressNoteEntity.setId(progressNote.getId());
        progressNoteEntity.setUserId(progressNote.getUserId());
        progressNoteEntity.setWeight(progressNote.getWeight());
        progressNoteEntity.setNote(progressNote.getNote());
        progressNoteEntity.setDate(progressNote.getDate());

        return progressNoteEntity;
    }
}
