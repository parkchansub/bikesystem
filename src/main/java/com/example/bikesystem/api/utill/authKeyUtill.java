package com.example.bikesystem.api.utill;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties()
public class authKeyUtill {

    String apiKey;
}
