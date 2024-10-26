package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.controllers.dto.ProgressNoteDTO;
import fitness_app_be.fitness_app.domain.ProgressNote;
import org.springframework.stereotype.Component;

@Component
public class ProgressNoteMapper {

    public ProgressNote toDomain(ProgressNoteDTO progressNoteDTO) {
        return new ProgressNote(progressNoteDTO.getId(), progressNoteDTO.getUserId(),
                progressNoteDTO.getWeight(), progressNoteDTO.getNote(),
                progressNoteDTO.getPictureURL());
    }

    public ProgressNoteDTO domainToDto(ProgressNote progressNote) {
        return new ProgressNoteDTO(progressNote.getId(), progressNote.getUserId(),
                progressNote.getWeight(), progressNote.getNote(),
                progressNote.getPictureURL());
    }
}
