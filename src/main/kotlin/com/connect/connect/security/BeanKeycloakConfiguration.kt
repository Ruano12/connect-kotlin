package com.connect.connect.security

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class BeanKeycloakConfiguration {

    @Bean
    @Primary
    fun keycloakConfigResolver(properties: KeycloakSpringBootProperties): KeycloakSpringBootConfigResolver =
        KeycloakSpringBootConfigResolver()
}