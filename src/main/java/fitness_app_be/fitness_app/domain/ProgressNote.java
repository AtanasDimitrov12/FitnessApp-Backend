package fitness_app_be.fitness_app.domain;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgressNote {
    @Setter(AccessLevel.NONE)
    private Long id;
    private Long userId;
    private double weight;
    private String note;
    private LocalDate date;

}
