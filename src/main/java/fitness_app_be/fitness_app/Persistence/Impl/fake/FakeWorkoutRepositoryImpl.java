package fitness_app_be.fitness_app.Persistence.Impl.fake;

import fitness_app_be.fitness_app.Domain.Enums.Exercises;
import fitness_app_be.fitness_app.Domain.Workout;
import fitness_app_be.fitness_app.Persistence.WorkoutRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class FakeWorkoutRepositoryImpl implements WorkoutRepository {

    private static long nextId = 1;
    private final List<Workout> savedWorkouts;

    public FakeWorkoutRepositoryImpl() {
        this.savedWorkouts = new ArrayList<>();
    }

    @Override
    public boolean exists(long id) {
        return savedWorkouts.stream().anyMatch(workout -> workout.getId() == id);
    }

    @Override
    public List<Workout> getAll() {
        return Collections.unmodifiableList(savedWorkouts);
    }

    @Override
    public Workout create(Workout workout) {
        workout.setId(nextId++);
        savedWorkouts.add(workout);
        return workout;
    }

    @Override
    public Workout update(Workout workout) {
        Optional<Workout> existingWorkout = getWorkoutById(workout.getId());
        if (existingWorkout.isPresent()) {
            Workout foundWorkout = existingWorkout.get();
            foundWorkout.setName(workout.getName());
            foundWorkout.setDescription(workout.getDescription());
            foundWorkout.setExercises(workout.getExercises());
            return foundWorkout;
        } else {
            throw new IllegalArgumentException("Workout not found");
        }
    }

    @Override
    public void delete(long workoutId) {
        Optional<Workout> existingWorkout = getWorkoutById(workoutId);
        existingWorkout.ifPresent(savedWorkouts::remove);
    }

    @Override
    public Optional<Workout> getWorkoutById(long workoutId) {
        return savedWorkouts.stream().filter(workout -> workout.getId() == workoutId).findFirst();
    }

    @Override
    public List<Workout> findByNameContainingIgnoreCase(String name) {
        List<Workout> matchingWorkouts = new ArrayList<>();
        for (Workout workout : savedWorkouts) {
            if (workout.getName().toLowerCase().contains(name.toLowerCase())) {
                matchingWorkouts.add(workout);
            }
        }
        return matchingWorkouts;
    }

    @Override
    public List<Workout> findByExercises(Exercises exercise) {
        List<Workout> matchingWorkouts = new ArrayList<>();
        for (Workout workout : savedWorkouts) {
            if (workout.getExercises().contains(exercise)) {
                matchingWorkouts.add(workout);
            }
        }
        return matchingWorkouts;
    }

    @Override
    public List<Workout> findByDescriptionContainingIgnoreCase(String keyword) {
        List<Workout> matchingWorkouts = new ArrayList<>();
        for (Workout workout : savedWorkouts) {
            if (workout.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                matchingWorkouts.add(workout);
            }
        }
        return matchingWorkouts;
    }
}

