package fitness_app_be.fitness_app.domain;


import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Workout {

    @Setter(AccessLevel.NONE)
    private Long id;
    private String name;
    private String description;
    private String pictureURL;
    private List<Exercise> exercises;
    private List<FitnessGoal> fitnessGoals;
    private List<FitnessLevel> fitnessLevels;
    private List<TrainingStyle> trainingStyles;
}