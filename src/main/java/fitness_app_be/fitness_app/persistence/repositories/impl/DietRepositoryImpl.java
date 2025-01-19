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
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
            List<MealEntity> managedMeals = new ArrayList<>(dietEntity.getMeals().stream()
                    .map(meal -> meal.getId() != null
                            ? jpaMealRepository.findById(meal.getId()).orElse(meal)
                            : meal)
                    .toList()); // Convert to mutable list
            dietEntity.setMeals(managedMeals);
        }

        DietEntity savedEntity = jpaDietRepository.save(dietEntity);
        return dietEntityMapperImpl.toDomain(savedEntity);
    }


    @Transactional
    @Override
    public Diet update(Diet diet) {
        DietEntity dietEntity = dietEntityMapperImpl.toEntity(diet);

        // Fetch all meals in a single query to avoid N+1 problem
        if (dietEntity.getMeals() != null) {
            Map<Long, MealEntity> existingMealsMap = jpaMealRepository.findAllById(
                    dietEntity.getMeals().stream()
                            .map(MealEntity::getId)
                            .filter(Objects::nonNull)
                            .toList()
            ).stream().collect(Collectors.toMap(MealEntity::getId, Function.identity()));

            List<MealEntity> managedMeals = dietEntity.getMeals().stream()
                    .map(meal -> meal.getId() != null
                            ? existingMealsMap.getOrDefault(meal.getId(), meal)
                            : meal)
                    .toList();
            dietEntity.setMeals(managedMeals);
        }

        // Save the updated DietEntity
        DietEntity updatedEntity = jpaDietRepository.save(dietEntity);

        // Map back to domain and return
        return dietEntityMapperImpl.toDomain(updatedEntity);
    }


    @Transactional
    @Override
    public void delete(long dietId) {
        jpaDietRepository.deleteById(dietId);
    }

    @Override
    public Optional<Diet> getDietById(long dietId) {
        return jpaDietRepository.findByIdWithMeals(dietId)
                .map(dietEntityMapperImpl::toDomain);
    }


    @Override
    @Transactional
    public Optional<Diet> getDietByUserId(long userId) {
        Optional<DietEntity> userDietEntity = jpaDietRepository.findDietEntityByUserId(userId);

        if (userDietEntity.isEmpty()) {
            return Optional.empty();
        }

        return jpaDietRepository.findByIdWithMeals(userDietEntity.get().getId())
                .map(dietEntityMapperImpl::toDomain);
    }



    @Override
    @Transactional
    public void addMealToDiet(Long dietId, Meal meal) {
        DietEntity dietEntity = jpaDietRepository.findById(dietId)
                .orElseThrow(() -> new IllegalArgumentException("Diet not found"));

        Hibernate.initialize(dietEntity.getMeals());

        List<MealEntity> mutableMeals = new ArrayList<>(dietEntity.getMeals()); // Ensure mutable list
        mutableMeals.add(mealEntityMapper.toEntity(meal));
        dietEntity.setMeals(mutableMeals); // Replace with updated list
        jpaDietRepository.save(dietEntity);
    }

    @Override
    @Transactional
    public void removeMealFromDiet(long dietId, long mealId) {
        DietEntity dietEntity = jpaDietRepository.findById(dietId)
                .orElseThrow(() -> new IllegalArgumentException("Diet not found"));

        Hibernate.initialize(dietEntity.getMeals());

        List<MealEntity> mutableMeals = new ArrayList<>(dietEntity.getMeals()); // Ensure mutable list
        mutableMeals.removeIf(meal -> meal.getId() == mealId);
        dietEntity.setMeals(mutableMeals); // Replace with updated list
        jpaDietRepository.save(dietEntity);
    }

}
