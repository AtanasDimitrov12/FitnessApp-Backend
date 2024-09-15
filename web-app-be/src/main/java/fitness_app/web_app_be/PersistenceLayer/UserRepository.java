package fitness_app.web_app_be.PersistenceLayer;

public interface UserRepository extends JpaRepository<User, Long> {
    // Custom query methods can be added here if needed
}
