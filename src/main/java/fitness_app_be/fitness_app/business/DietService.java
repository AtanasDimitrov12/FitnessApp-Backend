package fitness_app_be.fitness_app.business;

import fitness_app_be.fitness_app.domain.Diet;

import java.util.List;

public interface DietService {
    List<Diet> getAllDiets();
    Diet getDietById(Long id);
    Diet createDiet(Diet diet);
    void deleteDiet(Long id);
    Diet updateDiet(Diet diet);
}
