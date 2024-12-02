package fitness_app_be.fitness_app.persistence.repositories.impl;

import fitness_app_be.fitness_app.domain.ProgressNote;
import fitness_app_be.fitness_app.persistence.entity.ProgressNoteEntity;
import fitness_app_be.fitness_app.persistence.entity.UserEntity;
import fitness_app_be.fitness_app.persistence.jpa_repositories.JpaProgressNoteRepository;
import fitness_app_be.fitness_app.persistence.mapper.ProgressNoteEntityMapper;
import fitness_app_be.fitness_app.persistence.mapper.UserEntityMapper;
import fitness_app_be.fitness_app.persistence.repositories.ProgressNoteRepository;
import fitness_app_be.fitness_app.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProgressNoteRepositoryImpl implements ProgressNoteRepository {

    private final JpaProgressNoteRepository jpaProgressNoteRepository;
    private final ProgressNoteEntityMapper progressNoteEntityMapperImpl;
    private final UserRepository userRepository;
    private final UserEntityMapper mapper;

    @Override
    public boolean exists(long id) {
        return jpaProgressNoteRepository.existsById(id);
    }

    @Override
    public List<ProgressNote> getAll() {
        return jpaProgressNoteRepository.findAll().stream()
                .map(progressNoteEntityMapperImpl::toDomain)
                .toList();
    }

    @Override
    public ProgressNote create(ProgressNote progressNote) {
        // Fetch the user entity
        System.out.println(progressNote);

        UserEntity user = userRepository.findEntityById(progressNote.getUserId());

        // Map the domain object to an entity
        ProgressNoteEntity progressNoteEntity = progressNoteEntityMapperImpl.toEntity(progressNote);
        System.out.println("First "+progressNoteEntity);

        // Associate the progress note with the user
        progressNoteEntity.setUser(user);  // Ensure the relationship is established both ways
        System.out.println("Second "+progressNoteEntity);
        user.getNotes().add(progressNoteEntity);
        userRepository.update(mapper.toDomain(user));


        // Save the progress note (and potentially cascade the changes to the user if configured)
        ProgressNoteEntity savedEntity = jpaProgressNoteRepository.save(progressNoteEntity);

        // Return the domain object
        return progressNoteEntityMapperImpl.toDomain(savedEntity);
    }


    @Override
    public ProgressNote update(ProgressNote progressNote) {
        ProgressNoteEntity progressNoteEntity = progressNoteEntityMapperImpl.toEntity(progressNote);
        ProgressNoteEntity updatedEntity = jpaProgressNoteRepository.save(progressNoteEntity);
        return progressNoteEntityMapperImpl.toDomain(updatedEntity);
    }

    @Override
    public void delete(long progressNoteId) {
        jpaProgressNoteRepository.deleteById(progressNoteId);
    }

    @Override
    public Optional<ProgressNote> getProgressNoteById(long progressNoteId) {
        return jpaProgressNoteRepository.findById(progressNoteId)
                .map(progressNoteEntityMapperImpl::toDomain);
    }

    @Override
    public List<ProgressNote> findByUserId(Long userId) {
        return jpaProgressNoteRepository.findByUserId(userId).stream()
                .map(progressNoteEntityMapperImpl::toDomain)
                .toList();
    }
}
