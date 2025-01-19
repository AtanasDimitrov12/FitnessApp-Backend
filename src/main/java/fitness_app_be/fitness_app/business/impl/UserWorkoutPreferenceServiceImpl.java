package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.*;
import fitness_app_be.fitness_app.domain.User;
import fitness_app_be.fitness_app.domain.UserWorkoutPreference;
import fitness_app_be.fitness_app.domain.WorkoutPlan;
import fitness_app_be.fitness_app.exception_handling.UserWorkoutPreferenceNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.UserWorkoutPreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserWorkoutPreferenceServiceImpl implements UserWorkoutPreferenceService {

    private final UserWorkoutPreferenceRepository userWorkoutPreferenceRepository;
    private final WorkoutPlanGenerator workoutPlanGenerator;
    private final WorkoutPlanService workoutPlanService;
    private final UserService userService;

    @Override
    @Transactional
    public UserWorkoutPreference getUserWorkoutPreferenceByUserId(Long userId) {
        return userWorkoutPreferenceRepository.findByUserId(userId)
                .orElseThrow(() -> new UserWorkoutPreferenceNotFoundException(userId));
    }

    @Override
    @Transactional
    public UserWorkoutPreference createUserWorkoutPreference(UserWorkoutPreference userWorkoutPreference) {

        User user = userService.getUserById(userWorkoutPreference.getUserid());

        WorkoutPlan calculatedPlan = workoutPlanGenerator.calculateWorkoutPlan(userWorkoutPreference);

        if (calculatedPlan == null) {
            throw new IllegalStateException("Failed to calculate a workout plan for user preferences.");
        }

        calculatedPlan.setUserId(user.getId());
        WorkoutPlan newUserPlan = workoutPlanService.createWorkoutPlan(calculatedPlan);
        user.setWorkoutPlan(newUserPlan);
        userService.updateUser(user);

        userWorkoutPreference.setUserid(user.getId());
        return userWorkoutPreferenceRepository.create(userWorkoutPreference);
    }


    @Override
    @Transactional
    public void deleteUserWorkoutPreference(Long id) {
        // Check if the preference exists
        UserWorkoutPreference existingPreference = userWorkoutPreferenceRepository.getWorkoutPreferenceById(id)
                .orElseThrow(() -> new UserWorkoutPreferenceNotFoundException("Preference with ID " + id + " not found"));

        // Use the retrieved preference's ID to delete it
        userWorkoutPreferenceRepository.delete(existingPreference.getId());
    }



    @Override
    @Transactional
    public UserWorkoutPreference updateUserWorkoutPreference(UserWorkoutPreference userWorkoutPreference) {
        // Check if the UserWorkoutPreference exists and retrieve it
        UserWorkoutPreference existingPreference = userWorkoutPreferenceRepository.findByUserId(userWorkoutPreference.getUserid())
                .orElseThrow(() -> new UserWorkoutPreferenceNotFoundException("Preference with ID " + userWorkoutPreference.getId() + " not found"));

        existingPreference.setPreferredTrainingStyle(userWorkoutPreference.getPreferredTrainingStyle());
        existingPreference.setFitnessGoal(userWorkoutPreference.getFitnessGoal());
        existingPreference.setFitnessLevel(userWorkoutPreference.getFitnessLevel());
        existingPreference.setDaysAvailable(userWorkoutPreference.getDaysAvailable());
        // Fetch the user
        User user = userService.getUserById(existingPreference.getUserid());

        // Calculate the new workout plan
        WorkoutPlan existingPlan = workoutPlanService.getWorkoutPlanByUserId(existingPreference.getUserid());
        WorkoutPlan updatedPlan = workoutPlanGenerator.calculateWorkoutPlan(existingPreference);
        existingPlan.setWorkouts(updatedPlan.getWorkouts());

        // Save the updated workout plan
        WorkoutPlan savedUpdatedPlan = workoutPlanService.updateWorkoutPlan(existingPlan);

        // Update the user's workout plan
        user.setWorkoutPlan(savedUpdatedPlan);
        userService.updateUser(user);

        // Update the UserWorkoutPreference
        existingPreference.setUserid(user.getId());

        // Optionally log or perform additional operations with `existingPreference` if needed
        return userWorkoutPreferenceRepository.update(existingPreference);
    }

}
