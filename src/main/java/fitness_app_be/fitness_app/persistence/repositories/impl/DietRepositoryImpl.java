package fitness_app_be.fitness_app.persistence.repositories.impl;

import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.domain.Meal;
import fitness_app_be.fitness_app.persistence.entity.DietEntity;
import fitness_app_be.fitness_app.persistence.entity.MealEntity;
import fitness_app_be.fitness_app.persistence.jpa_repositories.JpaDietRepository;
import fitness_app_be.fitness_app.persistence.jpa_repositories.JpaMealRepository;
import fitness_app_be.fitness_app.persistence.mapper.DietEntityMapper;
import fitness_app_be.fitness_app.persistence.mapper.MealEntityMapper;
import fitness_app_be.fitness_app.persistence.repositories.DietRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.collection.spi.PersistentBag;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DietRepositoryImpl implements DietRepository {

    private final JpaDietRepository jpaDietRepository;
    private final JpaMealRepository jpaMealRepository;
    private final DietEntityMapper dietEntityMapperImpl;
    private final MealEntityMapper mealEntityMapper;

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

    @Transactional
    @Override
    public Diet create(Diet diet) {
        DietEntity dietEntity = dietEntityMapperImpl.toEntity(diet);

        if (dietEntity.getMeals() != null) {
            List<MealEntity> managedMeals = dietEntity.getMeals().stream()
                    .map(meal -> meal.getId() != null
                            ? jpaMealRepository.findById(meal.getId()).orElse(meal)
                            : meal)
                    .toList();
            dietEntity.setMeals(managedMeals);
        }

        DietEntity savedEntity = jpaDietRepository.save(dietEntity);
        return dietEntityMapperImpl.toDomain(savedEntity);
    }

    @Transactional
    @Override
    public Diet update(Diet diet) {
        DietEntity dietEntity = dietEntityMapperImpl.toEntity(diet);

        if (dietEntity.getMeals() != null) {
            List<MealEntity> managedMeals = dietEntity.getMeals().stream()
                    .map(meal -> meal.getId() != null
                            ? jpaMealRepository.findById(meal.getId()).orElse(meal)
                            : meal)
                    .toList();
            dietEntity.setMeals(managedMeals);
        }

        DietEntity updatedEntity = jpaDietRepository.save(dietEntity);
        return dietEntityMapperImpl.toDomain(updatedEntity);
    }

    @Transactional
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

    @Transactional
    @Override
    public void addMealToDiet(Long dietId, Meal meal) {
        MealEntity mealEntity = mealEntityMapper.toEntity(meal);
        DietEntity dietEntity = jpaDietRepository.findById(dietId)
                .orElseThrow(() -> new IllegalArgumentException("Diet not found"));

        Hibernate.initialize(dietEntity.getMeals()); // Ensure initialization

        if (dietEntity.getMeals() instanceof PersistentBag) {
            dietEntity.addMeal(mealEntity); // Modify collection safely
            jpaDietRepository.save(dietEntity);
        } else {
            throw new UnsupportedOperationException("Unexpected collection type");
        }
    }

    @Override
    @Transactional
    public void removeMealFromDiet(long dietId, long mealId) {
        DietEntity diet = jpaDietRepository.findById(dietId)
                .orElseThrow(() -> new IllegalArgumentException("Diet not found"));
        MealEntity meal = jpaMealRepository.findById(mealId)
                .orElseThrow(() -> new IllegalArgumentException("Meal not found"));
        diet.getMeals().remove(meal);
        jpaDietRepository.save(diet);
    }
}
