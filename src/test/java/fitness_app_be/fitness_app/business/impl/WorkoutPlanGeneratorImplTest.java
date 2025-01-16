package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.WorkoutService;
import fitness_app_be.fitness_app.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutPlanGeneratorImplTest {

    @Mock
    private WorkoutService workoutService;

    @InjectMocks
    private WorkoutPlanGeneratorImpl workoutPlanGenerator;

    private UserWorkoutPreference workoutPreference;
    private List<Workout> workouts;

    @BeforeEach
    void setUp() {
        workoutPreference = new UserWorkoutPreference(1L, 1L, FitnessGoal.MUSCLE_GAIN, FitnessLevel.INTERMEDIATE, TrainingStyle.STRENGTH, 4);
        workouts = List.of(
                new Workout(1L, "Workout1", "Description", "Picture", null, List.of(FitnessGoal.MUSCLE_GAIN), List.of(FitnessLevel.INTERMEDIATE), List.of(TrainingStyle.STRENGTH)),
                new Workout(2L, "Workout2", "Description", "Picture", null, List.of(FitnessGoal.MUSCLE_GAIN), List.of(FitnessLevel.INTERMEDIATE), List.of(TrainingStyle.STRENGTH)),
                new Workout(3L, "Workout3", "Description", "Picture", null, List.of(FitnessGoal.MUSCLE_GAIN), List.of(FitnessLevel.INTERMEDIATE), List.of(TrainingStyle.STRENGTH))
        );
    }

    @Test
    void calculateWorkoutPlan_ShouldReturnBestWorkoutPlan_WhenWorkoutsMatchPreference() {
        when(workoutService.getAllWorkouts()).thenReturn(workouts);

        WorkoutPlan result = workoutPlanGenerator.calculateWorkoutPlan(workoutPreference);

        assertNotNull(result);
        assertFalse(result.getWorkouts().isEmpty());
        verify(workoutService, times(1)).getAllWorkouts();
    }

    @Test
    void calculateWorkoutPlan_ShouldReturnNull_WhenNoMatchingWorkouts() {
        when(workoutService.getAllWorkouts()).thenReturn(new ArrayList<>());

        WorkoutPlan result = workoutPlanGenerator.calculateWorkoutPlan(workoutPreference);

        assertNull(result);
        verify(workoutService, times(1)).getAllWorkouts();
    }

    @Test
    void findMatchingWorkouts_ShouldReturnFilteredWorkouts() {
        when(workoutService.getAllWorkouts()).thenReturn(workouts);

        WorkoutPlan generatedPlan = workoutPlanGenerator.calculateWorkoutPlan(workoutPreference);
        assertNotNull(generatedPlan);
        List<Workout> filteredWorkouts = generatedPlan.getWorkouts();

        assertNotNull(filteredWorkouts);
        assertFalse(filteredWorkouts.isEmpty());
    }

    @Test
    void generateWorkoutPlans_ShouldReturnWorkoutPlans_WhenWorkoutsProvided() {
        when(workoutService.getAllWorkouts()).thenReturn(workouts);

        WorkoutPlan generatedPlan = workoutPlanGenerator.calculateWorkoutPlan(workoutPreference);
        assertNotNull(generatedPlan);
        List<WorkoutPlan> workoutPlans = List.of(generatedPlan);

        assertNotNull(workoutPlans);
        assertFalse(workoutPlans.isEmpty());
    }

    @Test
    void findBestWorkoutPlan_ShouldReturnBestMatch() {
        when(workoutService.getAllWorkouts()).thenReturn(workouts);

        WorkoutPlan bestPlan = workoutPlanGenerator.calculateWorkoutPlan(workoutPreference);

        assertNotNull(bestPlan);
        assertFalse(bestPlan.getWorkouts().isEmpty());
    }
}
