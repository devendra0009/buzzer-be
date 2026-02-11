package com.davendra.buzzer.config;

import com.cloudinary.Cloudinary;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProjectConfig {

    @Value("${secret.cloudName}")
    private String cloudName;

    @Value("${secret.apiKey}")
    private String apiKey;
    @Value("${secret.apiSecret}")
    private String apiSecret;

    @Bean
    public Cloudinary getCloudinary() {

        Map config = new HashMap();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        config.put("secure", true);
        return new Cloudinary(config);
    }

    @Bean
    public ModelMapper getModelMapper(){
        return new ModelMapper();
    }
}
