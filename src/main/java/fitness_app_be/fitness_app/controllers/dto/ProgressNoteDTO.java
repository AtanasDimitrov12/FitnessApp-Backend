package fitness_app_be.fitness_app.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgressNoteDTO {
    private Long id;
    private Long userId;
    private double weight;
    private String note;
    private String pictureURL;
}
