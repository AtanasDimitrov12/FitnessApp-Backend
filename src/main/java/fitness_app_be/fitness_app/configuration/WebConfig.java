package fitness_app_be.fitness_app.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Value("${corsheader}")
    private String corsHeader;

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        String[] corsHeaders = new String[1];
        corsHeaders[0] = corsHeader;

        registry.addMapping("/**")
                .allowedOrigins(corsHeaders)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}

