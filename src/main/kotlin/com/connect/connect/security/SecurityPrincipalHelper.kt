package com.connect.connect.security

import com.connect.connect.login.LoginProcessor
import org.keycloak.KeycloakPrincipal
import org.keycloak.KeycloakSecurityContext
import org.keycloak.adapters.OidcKeycloakAccount
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.keycloak.representations.AccessToken
import org.keycloak.representations.IDToken
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component
import java.util.*
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType
import java.util.UUID


@Component
class SecurityPrincipalHelper(val cachedUserInfoProvider:UserInfoOperations) {

    private val LOGGER: Logger = LoggerFactory.getLogger(SecurityPrincipalHelper::class.java);

    fun getFullName():Optional<String>{
        var optional:Optional<String> = this.getAccessToken().map(AccessToken::getName)
        LOGGER.debug("[SECURITY-HELPER] getName: {}", optional)
        return optional
    }

    fun getFirstName():Optional<String> {
        var optional:Optional<String> = this.getAccessToken().map(AccessToken::getGivenName)
        LOGGER.debug("[SECURITY-HELPER] getName: {}", optional)
        return optional
    }

    fun getLastName():Optional<String> {
        var optional:Optional<String> = this.getAccessToken().map(AccessToken::getFamilyName)
        LOGGER.debug("[SECURITY-HELPER] getLastName: {}", optional)
        return optional
    }

    fun getEmail():Optional<String>{
        var optional:Optional<String> = this.getAccessToken().map(AccessToken::getEmail)
        LOGGER.debug("[SECURITY-HELPER] getEmail: {}", optional)
        return optional
    }

    fun getUsername():String{
        var username:String = this.getAccessToken()
                                  .map(AccessToken::getSubject)
                                  .orElse("no-user")

        LOGGER.debug("[SECURITY-HELPER] getUsername: {}", username)
        return username
    }


    fun getPersonId(): Optional<UUID>? {
        var test:SecurityContext = SecurityContextHolder.getContext()
        var authentication:Authentication = test.authentication
        var kp:KeycloakPrincipal<KeycloakSecurityContext> = authentication.principal as KeycloakPrincipal<KeycloakSecurityContext>
        var token: IDToken? = kp.keycloakSecurityContext.token

        val otherClaims = token!!.otherClaims

        var personId:String = otherClaims.get(KeyCloakCustomFields.PERSON_ID.getId()).toString()

        return Optional.of(UUID.fromString(personId))
    }

    fun getOtherParameter(custom: KeyCloakCustomFields): Optional<Any> {

        val optional = Optional.ofNullable(SecurityContextHolder.getContext())
            .map { obj: SecurityContext -> obj.authentication }
            .filter { obj: Authentication -> obj.isAuthenticated }
            .map { o: Any? -> KeycloakAuthenticationToken::class.java.cast(o) }
            .map { obj: KeycloakAuthenticationToken -> obj.account }
            .map { obj: OidcKeycloakAccount -> obj.keycloakSecurityContext }
            .map { obj: KeycloakSecurityContext -> obj.tokenString }
            .map { accessToken: String -> cachedUserInfoProvider.get(accessToken) }
            .map { m -> m!![custom.getId()] }
        LOGGER.debug(
            "[SECURITY-HELPER] getOtherParameter, key: {}: value: {}", custom.getId(), optional
        )
        return optional
    }



    fun getAccessToken():Optional<AccessToken> = this.getKeycloakPrincipal()!!
                            .map{obj: KeycloakPrincipal<KeycloakSecurityContext> -> obj.keycloakSecurityContext}
                            .map(KeycloakSecurityContext::getToken)



    fun getKeycloakPrincipal() : Optional<KeycloakPrincipal<KeycloakSecurityContext>>? {
        var optional:Optional<KeycloakPrincipal<*>> = Optional.ofNullable(SecurityContextHolder.getContext())
                                        .map(SecurityContext::getAuthentication)
                                        .filter(Authentication::isAuthenticated)
                                        .map(Authentication::getPrincipal)
                                        .map(KeycloakPrincipal::class.java::cast)

        return optional as Optional<KeycloakPrincipal<KeycloakSecurityContext>>
    }


}








