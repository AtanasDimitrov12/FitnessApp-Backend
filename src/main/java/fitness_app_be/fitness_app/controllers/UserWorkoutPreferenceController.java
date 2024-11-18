package fitness_app_be.fitness_app.controllers;

import fitness_app_be.fitness_app.business.UserWorkoutPreferenceService;
import fitness_app_be.fitness_app.controllers.dto.UserWorkoutPreferenceDTO;
import fitness_app_be.fitness_app.controllers.mapper.UserWorkoutPreferenceMapper;
import fitness_app_be.fitness_app.domain.UserWorkoutPreference;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-workout-preferences")
@RequiredArgsConstructor
public class UserWorkoutPreferenceController {

    private final UserWorkoutPreferenceService userWorkoutPreferenceService;
    private final UserWorkoutPreferenceMapper userWorkoutPreferenceMapper;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public UserWorkoutPreferenceDTO getUserWorkoutPreferenceByUserId(@PathVariable Long userId) {
        UserWorkoutPreference userWorkoutPreference = userWorkoutPreferenceService.getUserWorkoutPreferenceByUserId(userId);
        return userWorkoutPreferenceMapper.toDto(userWorkoutPreference);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    public UserWorkoutPreferenceDTO createUserWorkoutPreference(@RequestBody UserWorkoutPreferenceDTO userWorkoutPreferenceDTO) {
        UserWorkoutPreference userWorkoutPreference = userWorkoutPreferenceMapper.toDomain(userWorkoutPreferenceDTO);
        UserWorkoutPreference createdUserWorkoutPreference = userWorkoutPreferenceService.createUserWorkoutPreference(userWorkoutPreference);
        return userWorkoutPreferenceMapper.toDto(createdUserWorkoutPreference);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping
    public UserWorkoutPreferenceDTO updateUserWorkoutPreference(@RequestBody UserWorkoutPreferenceDTO userWorkoutPreferenceDTO) {
        UserWorkoutPreference userWorkoutPreference = userWorkoutPreferenceMapper.toDomain(userWorkoutPreferenceDTO);
        UserWorkoutPreference updatedUserWorkoutPreference = userWorkoutPreferenceService.updateUserWorkoutPreference(userWorkoutPreference);
        return userWorkoutPreferenceMapper.toDto(updatedUserWorkoutPreference);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserWorkoutPreference(@PathVariable Long id) {
        userWorkoutPreferenceService.deleteUserWorkoutPreference(id);
        return ResponseEntity.noContent().build();
    }
}
