package fitness_app.web_app_be.BusinessLayer;

import fitness_app.web_app_be.DTOsLayer.UserDTO;
import fitness_app.web_app_be.ExceptionHandlingLayer.UserNotFoundException;
import fitness_app.web_app_be.MapperLayer.UserMapper;
import fitness_app.web_app_be.PersistenceLayer.Entity.User;
import fitness_app.web_app_be.PersistenceLayer.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Lombok will generate a constructor with required arguments (final fields)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}


