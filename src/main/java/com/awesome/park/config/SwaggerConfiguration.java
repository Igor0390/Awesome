package com.awesome.park.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Awesome Park booking Api",
                description = "Приложение для записи пользователей", version = "1.0.0",
                contact = @Contact(
                        name = "Kirill Gorkiy",
                        email = "Gorkiy@list.ru",
                        url = "https://t.me/Gorkiy7"
                )
        )
)
@Configuration
public class SwaggerConfiguration {

}

