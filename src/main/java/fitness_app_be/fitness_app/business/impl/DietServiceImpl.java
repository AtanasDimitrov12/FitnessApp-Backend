package fitness_app_be.fitness_app.business.impl;

import fitness_app_be.fitness_app.business.DietService;
import fitness_app_be.fitness_app.domain.Diet;
import fitness_app_be.fitness_app.exception_handling.DietNotFoundException;
import fitness_app_be.fitness_app.persistence.repositories.DietRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DietServiceImpl implements DietService {

    private final DietRepository dietRepository;

    @Override
    public List<Diet> getAllDiets() {
        return dietRepository.getAll();
    }

    @Override
    public Diet getDietById(Long id) {
        return dietRepository.getDietById(id)
                .orElseThrow(() -> new DietNotFoundException(id));
    }

    @Override
    public Diet createDiet(Diet diet) {
        return dietRepository.create(diet);
    }

    @Override
    public void deleteDiet(Long id) {
        if (!dietRepository.exists(id)) {
            throw new DietNotFoundException(id);
        }
        dietRepository.delete(id);
    }

    @Override
    public Diet updateDiet(Diet diet) {
        Diet existingDiet = dietRepository.getDietById(diet.getId())
                .orElseThrow(() -> new DietNotFoundException("Diet with ID " + diet.getId() + " not found"));

        existingDiet.setName(diet.getName());
        existingDiet.setDescription(diet.getDescription());
        existingDiet.setMeals(diet.getMeals());

        return dietRepository.update(existingDiet);
    }
}
