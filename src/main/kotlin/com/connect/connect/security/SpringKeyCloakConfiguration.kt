package com.connect.connect.security

import org.keycloak.adapters.springsecurity.KeycloakConfiguration
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import kotlin.jvm.Throws


@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@KeycloakConfiguration
class SpringKeyCloakConfiguration : KeycloakWebSecurityConfigurerAdapter() {
    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder,
                        keycloakAuthenticationProvider:UserInfoKeycloakAuthenticationProvider) {

        val grantedAuthorityMapper = SimpleAuthorityMapper()
        grantedAuthorityMapper.setPrefix("ROLE_")
        grantedAuthorityMapper.setConvertToUpperCase(true)
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(grantedAuthorityMapper)
        auth.authenticationProvider(keycloakAuthenticationProvider)
    }

    @Bean
    override fun sessionAuthenticationStrategy(): SessionAuthenticationStrategy {
        return RegisterSessionAuthenticationStrategy(SessionRegistryImpl())
    }

    @Bean
    @Throws(Exception::class)
    override fun keycloakAuthenticationProcessingFilter(): KeycloakAuthenticationProcessingFilter {
        val requestMatcher: RequestMatcher = OrRequestMatcher(KeycloakAuthenticationProcessingFilter.DEFAULT_REQUEST_MATCHER)
        val filter = KeycloakAuthenticationProcessingFilter(
                super.authenticationManagerBean(), requestMatcher)
        filter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy())
        return filter
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        super.configure(http)
        http.anonymous()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .anyRequest()
                .fullyAuthenticated()
                .and()
                .csrf()
                .disable()
                .httpBasic()
                .disable()
    }

    @Throws(Exception::class)
    override fun configure(web: WebSecurity) {
        web.ignoring()
                .antMatchers(
                        "/v1/_status/healthz",
                        "/login",
                        "/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/**",
                        "/swagger-ui.html",
                        "/webjars/**")
    }
}