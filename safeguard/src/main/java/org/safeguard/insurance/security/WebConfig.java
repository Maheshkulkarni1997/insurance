package org.safeguard.insurance.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Apply CORS configuration to all endpoints
            .allowedOrigins("http://127.0.0.1:5500") // Specify the allowed origin
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
            .allowedHeaders("Content-Type", "Authorization") // Allowed headers
            .allowCredentials(true); // Allow credentials (cookies, etc.)
    }
}
