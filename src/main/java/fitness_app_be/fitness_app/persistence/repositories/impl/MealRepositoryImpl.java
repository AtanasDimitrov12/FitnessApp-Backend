package fitness_app_be.fitness_app.persistence.repositories.impl;

import fitness_app_be.fitness_app.domain.Meal;
import fitness_app_be.fitness_app.persistence.entity.MealEntity;
import fitness_app_be.fitness_app.persistence.jpa_repositories.JpaMealRepository;
import fitness_app_be.fitness_app.persistence.mapper.MealEntityMapper;
import fitness_app_be.fitness_app.persistence.repositories.MealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MealRepositoryImpl implements MealRepository {

    private final JpaMealRepository jpaMealRepository;
    private final MealEntityMapper mealEntityMapperImpl;

    @Override
    public boolean exists(long id) {
        return jpaMealRepository.existsById(id);
    }

    @Override
    public List<Meal> getAll() {
        return jpaMealRepository.findAll().stream()
                .map(mealEntityMapperImpl::toDomain)
                .toList();
    }

    @Override
    public Meal create(Meal meal) {
        MealEntity mealEntity = mealEntityMapperImpl.toEntity(meal);
        MealEntity savedEntity = jpaMealRepository.save(mealEntity);
        return mealEntityMapperImpl.toDomain(savedEntity);
    }

    @Override
    public Meal update(Meal meal) {
        MealEntity mealEntity = mealEntityMapperImpl.toEntity(meal);
        MealEntity updatedEntity = jpaMealRepository.save(mealEntity);
        return mealEntityMapperImpl.toDomain(updatedEntity);
    }

    @Override
    public void delete(long mealId) {
        jpaMealRepository.deleteById(mealId);
    }

    @Override
    public Optional<Meal> getMealById(long mealId) {
        return jpaMealRepository.findById(mealId)
                .map(mealEntityMapperImpl::toDomain);
    }

    @Override
    public List<Meal> findByNameContainingIgnoreCase(String name) {
        return jpaMealRepository.findByNameContainingIgnoreCase(name).stream()
                .map(mealEntityMapperImpl::toDomain)
                .toList();
    }
}
