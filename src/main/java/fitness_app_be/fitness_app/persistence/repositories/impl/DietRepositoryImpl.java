package fitness_app_be.fitness_app.persistence.repositories.impl;

import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.persistence.entity.DietEntity;
import fitness_app_be.fitness_app.persistence.jpa_repositories.JpaDietRepository;
import fitness_app_be.fitness_app.persistence.mapper.DietEntityMapper;
import fitness_app_be.fitness_app.persistence.repositories.DietRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DietRepositoryImpl implements DietRepository {

    private final JpaDietRepository jpaDietRepository;
    private final DietEntityMapper dietEntityMapperImpl;

    @Override
    public boolean exists(long id) {
        return jpaDietRepository.existsById(id);
    }

    @Override
    public List<Diet> getAll() {
        return jpaDietRepository.findAll().stream()
                .map(dietEntityMapperImpl::toDomain)
                .toList();
    }

    @Override
    public Diet create(Diet diet) {
        DietEntity dietEntity = dietEntityMapperImpl.toEntity(diet);
        DietEntity savedEntity = jpaDietRepository.save(dietEntity);
        return dietEntityMapperImpl.toDomain(savedEntity);
    }

    @Override
    public Diet update(Diet diet) {
        DietEntity dietEntity = dietEntityMapperImpl.toEntity(diet);
        DietEntity updatedEntity = jpaDietRepository.save(dietEntity);
        return dietEntityMapperImpl.toDomain(updatedEntity);
    }

    @Override
    public void delete(long dietId) {
        jpaDietRepository.deleteById(dietId);
    }

    @Override
    public Optional<Diet> getDietById(long dietId) {
        return jpaDietRepository.findById(dietId)
                .map(dietEntityMapperImpl::toDomain);
    }

    @Override
    public Optional<Diet> getDietByUserId(long userId) {
        return jpaDietRepository.findDietEntitiesByUserId(userId)
                .map(dietEntityMapperImpl::toDomain);
    }

}
