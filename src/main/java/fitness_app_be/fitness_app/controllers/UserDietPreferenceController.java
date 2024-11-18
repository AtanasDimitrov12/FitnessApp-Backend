package fitness_app_be.fitness_app.controllers;

import fitness_app_be.fitness_app.business.UserDietPreferenceService;
import fitness_app_be.fitness_app.controllers.dto.UserDietPreferenceDTO;
import fitness_app_be.fitness_app.controllers.mapper.UserDietPreferenceMapper;
import fitness_app_be.fitness_app.domain.UserDietPreference;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-diet-preferences")
@RequiredArgsConstructor
public class UserDietPreferenceController {

    private final UserDietPreferenceService userDietPreferenceService;
    private final UserDietPreferenceMapper userDietPreferenceMapper;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public UserDietPreferenceDTO getUserDietPreferenceByUserId(@PathVariable Long userId) {
        UserDietPreference userDietPreference = userDietPreferenceService.getUserDietPreferenceByUserId(userId);
        return userDietPreferenceMapper.toDto(userDietPreference);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    public UserDietPreferenceDTO createUserDietPreference(@RequestBody UserDietPreferenceDTO userDietPreferenceDTO) {
        UserDietPreference userDietPreference = userDietPreferenceMapper.toDomain(userDietPreferenceDTO);
        UserDietPreference createdUserDietPreference = userDietPreferenceService.createUserDietPreference(userDietPreference);
        return userDietPreferenceMapper.toDto(createdUserDietPreference);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping
    public UserDietPreferenceDTO updateUserDietPreference(@RequestBody UserDietPreferenceDTO userDietPreferenceDTO) {
        UserDietPreference userDietPreference = userDietPreferenceMapper.toDomain(userDietPreferenceDTO);
        UserDietPreference updatedUserDietPreference = userDietPreferenceService.updateUserDietPreference(userDietPreference);
        return userDietPreferenceMapper.toDto(updatedUserDietPreference);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserDietPreference(@PathVariable Long id) {
        userDietPreferenceService.deleteUserDietPreference(id);
        return ResponseEntity.noContent().build();
    }
}
