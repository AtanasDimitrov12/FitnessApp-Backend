package fitness_app_be.fitness_app.configuration.security.token;

import java.util.Set;

/**
 * Represents an authentication token providing information about the authenticated user.
 * This interface allows flexibility for different token implementations (e.g., JWT, OAuth).
 */
public interface AccessToken {
    /**
     * Gets the subject of the token (e.g., username or email).
     *
     * @return the subject as a string
     */
    String getSubject();

    /**
     * Gets the roles associated with the token.
     *
     * @return a set of roles
     */
    Set<String> getRoles();

    /**
     * Gets the user ID associated with the token.
     *
     * @return the user ID
     */
    Long getUserId();
}
