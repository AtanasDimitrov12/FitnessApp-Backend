package fitness_app_be.fitness_app.configuration.security.token.impl;


import fitness_app_be.fitness_app.configuration.security.token.AccessToken;

import java.util.Set;

public class JwtAccessToken implements AccessToken {
    private final String subject;
    private final Set<String> roles;
    private final Long userId;

    public JwtAccessToken(String subject, Set<String> roles, Long userId) {
        this.subject = subject;
        this.roles = roles;
        this.userId = userId;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public Set<String> getRoles() {
        return roles;
    }

    @Override
    public Long getUserId() {
        return userId;
    }
}

