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
        // Validate user existence
        System.out.println("Create workout preference");
        User user = userService.getUserById(userWorkoutPreference.getUserid());

        // Calculate the workout plan based on user preferences
        WorkoutPlan calculatedPlan = workoutPlanGenerator.calculateWorkoutPlan(userWorkoutPreference);
        calculatedPlan.setUserId(user.getId());
        WorkoutPlan newUserPlan = workoutPlanService.createWorkoutPlan(calculatedPlan);
        user.setWorkoutPlan(newUserPlan);
        userService.updateUser(user);
        // Optionally save or associate the plan with the user
        userWorkoutPreference.setUserid(user.getId());

        // Persist UserWorkoutPreference
        return userWorkoutPreferenceRepository.create(userWorkoutPreference);
    }

    @Override
    @Transactional
    public void deleteUserWorkoutPreference(Long id) {
        // Validate existence before deleting
        UserWorkoutPreference userWorkoutPreference = userWorkoutPreferenceRepository.getWorkoutPreferenceById(id)
                .orElseThrow(() -> new UserWorkoutPreferenceNotFoundException(id));

        // Delete the UserWorkoutPreference
        userWorkoutPreferenceRepository.delete(id);

        // Optionally handle cascading cleanup of associated workout plans if required
        // workoutService.deleteWorkoutPlan(userWorkoutPreference.getWorkoutPlanId());
    }

    @Override
    @Transactional
    public UserWorkoutPreference updateUserWorkoutPreference(UserWorkoutPreference userWorkoutPreference) {
        // Validate the existence of the UserWorkoutPreference to update
        System.out.println("Update workout preference");
        UserWorkoutPreference existingPreference = userWorkoutPreferenceRepository.getWorkoutPreferenceById(userWorkoutPreference.getId())
                .orElseThrow(() -> new UserWorkoutPreferenceNotFoundException(userWorkoutPreference.getId()));

        // Fetch the user associated with the preference
        User user = userService.getUserById(userWorkoutPreference.getUserid());

        // Recalculate the workout plan based on updated preferences
        WorkoutPlan updatedPlan = workoutPlanGenerator.calculateWorkoutPlan(userWorkoutPreference);
        updatedPlan.setUserId(user.getId());
        WorkoutPlan savedUpdatedPlan = workoutPlanService.createWorkoutPlan(updatedPlan);

        // Update the user's workout plan
        user.setWorkoutPlan(savedUpdatedPlan);
        userService.updateUser(user);

        // Persist the updated UserWorkoutPreference with recalculated details
        userWorkoutPreference.setUserid(user.getId());
        return userWorkoutPreferenceRepository.update(userWorkoutPreference);
    }
}
