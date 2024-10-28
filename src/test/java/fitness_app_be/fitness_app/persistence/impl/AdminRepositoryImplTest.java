package fitness_app_be.fitness_app.persistence.impl;

import fitness_app_be.fitness_app.domain.Admin;
import fitness_app_be.fitness_app.persistence.entity.AdminEntity;
import fitness_app_be.fitness_app.persistence.jpaRepositories.JpaAdminRepository;
import fitness_app_be.fitness_app.persistence.mapper.AdminEntityMapper;
import fitness_app_be.fitness_app.persistence.repositories.impl.AdminRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminRepositoryImplTest {

    @Mock
    private JpaAdminRepository jpaAdminRepository;

    @Mock
    private AdminEntityMapper adminEntityMapper;

    @InjectMocks
    private AdminRepositoryImpl adminRepository;

    private Admin admin;
    private AdminEntity adminEntity;

    @BeforeEach
    void setUp() {
        admin = new Admin(1L, "admin@example.com", "password");


        adminEntity = new AdminEntity();
        adminEntity.setId(1L);
        adminEntity.setEmail("admin@example.com");
    }

    @Test
    void exists_ShouldReturnTrue_WhenAdminExists() {
        when(jpaAdminRepository.existsById(1L)).thenReturn(true);

        assertTrue(adminRepository.exists(1L));
        verify(jpaAdminRepository, times(1)).existsById(1L);
    }

    @Test
    void exists_ShouldReturnFalse_WhenAdminDoesNotExist() {
        when(jpaAdminRepository.existsById(1L)).thenReturn(false);

        assertFalse(adminRepository.exists(1L));
        verify(jpaAdminRepository, times(1)).existsById(1L);
    }

    @Test
    void getAll_ShouldReturnListOfAdmins() {
        when(jpaAdminRepository.findAll()).thenReturn(List.of(adminEntity));
        when(adminEntityMapper.toDomain(adminEntity)).thenReturn(admin);

        List<Admin> admins = adminRepository.getAll();

        assertNotNull(admins);
        assertEquals(1, admins.size());
        assertEquals(admin, admins.get(0));
        verify(jpaAdminRepository, times(1)).findAll();
        verify(adminEntityMapper, times(1)).toDomain(adminEntity);
    }

    @Test
    void create_ShouldReturnCreatedAdmin() {
        when(adminEntityMapper.toEntity(admin)).thenReturn(adminEntity);
        when(jpaAdminRepository.save(adminEntity)).thenReturn(adminEntity);
        when(adminEntityMapper.toDomain(adminEntity)).thenReturn(admin);

        Admin createdAdmin = adminRepository.create(admin);

        assertNotNull(createdAdmin);
        assertEquals(admin, createdAdmin);
        verify(jpaAdminRepository, times(1)).save(adminEntity);
        verify(adminEntityMapper, times(1)).toEntity(admin);
        verify(adminEntityMapper, times(1)).toDomain(adminEntity);
    }

    @Test
    void update_ShouldReturnUpdatedAdmin() {
        when(adminEntityMapper.toEntity(admin)).thenReturn(adminEntity);
        when(jpaAdminRepository.save(adminEntity)).thenReturn(adminEntity);
        when(adminEntityMapper.toDomain(adminEntity)).thenReturn(admin);

        Admin updatedAdmin = adminRepository.update(admin);

        assertNotNull(updatedAdmin);
        assertEquals(admin, updatedAdmin);
        verify(jpaAdminRepository, times(1)).save(adminEntity);
        verify(adminEntityMapper, times(1)).toEntity(admin);
        verify(adminEntityMapper, times(1)).toDomain(adminEntity);
    }

    @Test
    void delete_ShouldDeleteAdminById() {
        doNothing().when(jpaAdminRepository).deleteById(1L);

        adminRepository.delete(1L);

        verify(jpaAdminRepository, times(1)).deleteById(1L);
    }

    @Test
    void getAdminById_ShouldReturnAdmin_WhenAdminExists() {
        when(jpaAdminRepository.findById(1L)).thenReturn(Optional.of(adminEntity));
        when(adminEntityMapper.toDomain(adminEntity)).thenReturn(admin);

        Optional<Admin> foundAdmin = adminRepository.getAdminById(1L);

        assertTrue(foundAdmin.isPresent());
        assertEquals(admin, foundAdmin.get());
        verify(jpaAdminRepository, times(1)).findById(1L);
        verify(adminEntityMapper, times(1)).toDomain(adminEntity);
    }

    @Test
    void getAdminById_ShouldReturnEmpty_WhenAdminDoesNotExist() {
        when(jpaAdminRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Admin> foundAdmin = adminRepository.getAdminById(1L);

        assertTrue(foundAdmin.isEmpty());
        verify(jpaAdminRepository, times(1)).findById(1L);
        verify(adminEntityMapper, never()).toDomain(any());
    }

    @Test
    void findByEmail_ShouldReturnAdmin_WhenAdminWithEmailExists() {
        when(jpaAdminRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(adminEntity));
        when(adminEntityMapper.toDomain(adminEntity)).thenReturn(admin);

        Optional<Admin> foundAdmin = adminRepository.findByEmail("admin@example.com");

        assertTrue(foundAdmin.isPresent());
        assertEquals(admin, foundAdmin.get());
        verify(jpaAdminRepository, times(1)).findByEmail("admin@example.com");
        verify(adminEntityMapper, times(1)).toDomain(adminEntity);
    }

    @Test
    void findByEmail_ShouldReturnEmpty_WhenAdminWithEmailDoesNotExist() {
        when(jpaAdminRepository.findByEmail("admin@example.com")).thenReturn(Optional.empty());

        Optional<Admin> foundAdmin = adminRepository.findByEmail("admin@example.com");

        assertTrue(foundAdmin.isEmpty());
        verify(jpaAdminRepository, times(1)).findByEmail("admin@example.com");
        verify(adminEntityMapper, never()).toDomain(any());
    }
}
