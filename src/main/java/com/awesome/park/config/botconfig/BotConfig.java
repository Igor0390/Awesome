package com.awesome.park.config.botconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotBlank;

@ConstructorBinding
@ConfigurationProperties(prefix = "application.telegram")
public record BotConfig(@NotBlank String name,
                        @NotBlank String token) {
}