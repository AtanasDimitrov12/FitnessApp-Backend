package fitness_app_be.fitness_app.BusinessLayer.Impl;

import fitness_app_be.fitness_app.BusinessLayer.UserService;
import fitness_app_be.fitness_app.DTOsLayer.UserDTO;
import fitness_app_be.fitness_app.ExceptionHandlingLayer.UserNotFoundException;
import fitness_app_be.fitness_app.MapperLayer.UserMapper;
import fitness_app_be.fitness_app.PersistenceLayer.Entity.UserEntity;
import fitness_app_be.fitness_app.PersistenceLayer.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        UserEntity userEntity = userMapper.toEntity(userDTO);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        return userMapper.toDto(savedUserEntity);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

    @Override
    public List<UserDTO> searchUsersByPartialUsername(String partialUsername) {
        List<UserEntity> userEntities = userRepository.findByUsernameContainingIgnoreCase(partialUsername);
        return userEntities.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        UserEntity existingUserEntity = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User with email " + userDTO.getEmail() + " not found"));

        // Update fields from the UserDTO to the existing User
        existingUserEntity.setUsername(userDTO.getUsername());
        existingUserEntity.setFitnessGoal(userDTO.getFitnessGoal());
        existingUserEntity.setDietPreference(userDTO.getDietPreference());


        UserEntity updatedUserEntity = userRepository.save(existingUserEntity);
        return userMapper.toDto(updatedUserEntity);
    }



}
