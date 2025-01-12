package fitness_app_be.fitness_app.configuration.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Allow CORS for the frontend origin explicitly
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:5173", "http://localhost:5174") // Replace with frontend URL
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Destination prefix for subscribing to topics
        config.setApplicationDestinationPrefixes("/app"); // Prefix for client messages
    }
}
