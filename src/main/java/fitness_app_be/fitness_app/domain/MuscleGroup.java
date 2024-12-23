package fitness_app_be.fitness_app.domain;

public enum MuscleGroup {
    BACK("back"),
    CARDIO("cardio"),
    CHEST("chest"),
    LOWER_ARMS("lower arms"),
    LOWER_LEGS("lower legs"),
    NECK("neck"),
    SHOULDERS("shoulders"),
    UPPER_ARMS("upper arms"),
    UPPER_LEGS("upper legs"),
    ABS("abs"),
    LATS("lats"),
    PECTORALS("pectorals"),
    WAIST("waist"),
    UNKNOWN("unknown");

    private final String displayName;

    MuscleGroup(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

