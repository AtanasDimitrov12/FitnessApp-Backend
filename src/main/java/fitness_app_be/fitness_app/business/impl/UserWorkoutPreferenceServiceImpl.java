package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.UserWorkoutPreferenceService;
import fitness_app_be.fitness_app.business.WorkoutPlanGenerator;
import fitness_app_be.fitness_app.business.WorkoutService;
import fitness_app_be.fitness_app.business.UserService;
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
    private final WorkoutService workoutService;
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
        // Recalculate the workout plan based on updated preferences
        System.out.println("Update workout preference");
        WorkoutPlan recalculatedPlan = workoutPlanGenerator.calculateWorkoutPlan(userWorkoutPreference);

        // Fetch and update the existing user if necessary
        User user = userService.getUserById(userWorkoutPreference.getUserid());

        // Optionally save or update the new workout plan (if directly associated with User)
        // workoutService.updateWorkoutPlan(recalculatedPlan);

        // Persist the updated UserWorkoutPreference
        userWorkoutPreference.setUserid(user.getId());
        return userWorkoutPreferenceRepository.update(userWorkoutPreference);
    }
}
