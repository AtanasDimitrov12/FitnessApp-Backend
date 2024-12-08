package fitness_app_be.fitness_app.controllers.mapper;

import fitness_app_be.fitness_app.controllers.dto.DietDTO;
import fitness_app_be.fitness_app.domain.Diet;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@AllArgsConstructor
public class DietMapper {

    private final UserMapper userMapper;

    public Diet toDomain(DietDTO dietDTO) {
        if (dietDTO == null) {
            return null;
        }

        return Diet.builder()
                .id(dietDTO.getId())
                .user(dietDTO.getUser() != null ? userMapper.toDomain(dietDTO.getUser()) : null) // Map single User
                .meals(new ArrayList<>()) // Avoid mapping meals here
                .build();
    }

    public DietDTO domainToDto(Diet diet) {
        if (diet == null) {
            return null;
        }

        return DietDTO.builder()
                .id(diet.getId())
                .user(diet.getUser() != null ? userMapper.domainToDto(diet.getUser()) : null) // Map single User
                .meals(new ArrayList<>()) // Avoid mapping meals here
                .build();
    }
}
