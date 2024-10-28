package fitness_app_be.fitness_app.persistence.impl;

import fitness_app_be.fitness_app.domain.Admin;
import fitness_app_be.fitness_app.persistence.entity.AdminEntity;
import fitness_app_be.fitness_app.persistence.jpaRepositories.JpaAdminRepository;
import fitness_app_be.fitness_app.persistence.mapper.AdminEntityMapper;
import fitness_app_be.fitness_app.persistence.repositories.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AdminRepositoryImpl implements AdminRepository {

    private final JpaAdminRepository jpaAdminRepository;
    private final AdminEntityMapper adminEntityMapper;

    @Override
    public boolean exists(long id) {
        return jpaAdminRepository.existsById(id);
    }

    @Override
    public List<Admin> getAll() {
        return jpaAdminRepository.findAll().stream()
                .map(adminEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Admin create(Admin trainer) {
        AdminEntity adminEntity = adminEntityMapper.toEntity(trainer);
        AdminEntity savedEntity = jpaAdminRepository.save(adminEntity);
        return adminEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Admin update(Admin trainer) {
        AdminEntity adminEntity = adminEntityMapper.toEntity(trainer);
        AdminEntity updatedEntity = jpaAdminRepository.save(adminEntity);
        return adminEntityMapper.toDomain(updatedEntity);
    }

    @Override
    public void delete(long trainerId) {
        jpaAdminRepository.deleteById(trainerId);
    }



    @Override
    public Optional<Admin> getAdminById(long adminId) {
        return jpaAdminRepository.findById(adminId)
                .map(adminEntityMapper::toDomain);
    }


    @Override
    public Optional<Admin> findByEmail(String email){
        return jpaAdminRepository.findByEmail(email)
                .map(adminEntityMapper::toDomain);
    }
}
