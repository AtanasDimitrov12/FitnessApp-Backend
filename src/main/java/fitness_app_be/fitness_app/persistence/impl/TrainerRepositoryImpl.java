package fitness_app_be.fitness_app.persistence.impl;

import fitness_app_be.fitness_app.domain.Trainer;
import fitness_app_be.fitness_app.persistence.entity.TrainerEntity;
import fitness_app_be.fitness_app.persistence.jpaRepositories.JpaTrainerRepository;
import fitness_app_be.fitness_app.persistence.mapper.TrainerEntityMapper;
import fitness_app_be.fitness_app.persistence.repositories.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TrainerRepositoryImpl implements TrainerRepository {

    private final JpaTrainerRepository jpaTrainerRepository;
    private final TrainerEntityMapper trainerEntityMapper;

    @Override
    public boolean exists(long id) {
        return jpaTrainerRepository.existsById(id);
    }

    @Override
    public List<Trainer> getAll() {
        return jpaTrainerRepository.findAll().stream()
                .map(trainerEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Trainer create(Trainer trainer) {
        TrainerEntity trainerEntity = trainerEntityMapper.toEntity(trainer);
        TrainerEntity savedEntity = jpaTrainerRepository.save(trainerEntity);
        return trainerEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Trainer update(Trainer trainer) {
        TrainerEntity trainerEntity = trainerEntityMapper.toEntity(trainer);
        TrainerEntity updatedEntity = jpaTrainerRepository.save(trainerEntity);
        return trainerEntityMapper.toDomain(updatedEntity);
    }

    @Override
    public void delete(long trainerId) {
        jpaTrainerRepository.deleteById(trainerId);
    }

    @Override
    public Optional<Trainer> getTrainerById(long trainerId) {
        return jpaTrainerRepository.findById(trainerId)
                .map(trainerEntityMapper::toDomain);
    }

    @Override
    public Optional<Trainer> findByEmail(String email) {
        return jpaTrainerRepository.findByEmail(email)
                .map(trainerEntityMapper::toDomain);
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        return jpaTrainerRepository.findByUsername(username)
                .map(trainerEntityMapper::toDomain);
    }

    @Override
    public List<Trainer> findByExpertise(String expertise) {
        return jpaTrainerRepository.findByExpertiseContainingIgnoreCase(expertise).stream()
                .map(trainerEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Trainer> findByUsernameContainingIgnoreCase(String partialUsername) {
        return jpaTrainerRepository.findByUsernameContainingIgnoreCase(partialUsername).stream()
                .map(trainerEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long countByEmail(String email) {
        return jpaTrainerRepository.countByEmail(email);
    }
}
