package fitness_app_be.fitness_app.persistence.repositories.impl;

import fitness_app_be.fitness_app.domain.ProgressNote;
import fitness_app_be.fitness_app.persistence.entity.ProgressNoteEntity;
import fitness_app_be.fitness_app.persistence.jpa_repositories.JpaProgressNoteRepository;
import fitness_app_be.fitness_app.persistence.mapper.ProgressNoteEntityMapper;
import fitness_app_be.fitness_app.persistence.repositories.ProgressNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProgressNoteRepositoryImpl implements ProgressNoteRepository {

    private final JpaProgressNoteRepository jpaProgressNoteRepository;
    private final ProgressNoteEntityMapper progressNoteEntityMapperImpl;

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
        ProgressNoteEntity progressNoteEntity = progressNoteEntityMapperImpl.toEntity(progressNote);
        ProgressNoteEntity savedEntity = jpaProgressNoteRepository.save(progressNoteEntity);
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
