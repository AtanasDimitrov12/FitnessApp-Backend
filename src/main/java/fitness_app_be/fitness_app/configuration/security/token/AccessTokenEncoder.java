package fitness_app_be.fitness_app.configuration.security.token;

public interface AccessTokenEncoder {
    String encode(AccessToken accessToken);
}
