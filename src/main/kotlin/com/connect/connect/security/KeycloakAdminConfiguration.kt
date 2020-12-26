package com.connect.connect.security

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder
import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.representations.AccessTokenResponse
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean
import java.util.concurrent.TimeUnit
import javax.ws.rs.NotAuthorizedException

@Configuration
@ConfigurationProperties(prefix = "admin.keycloak")
class KeycloakAdminConfiguration {

    private val LOGGER:Logger = LoggerFactory.getLogger(KeycloakAdminConfiguration::class.java)

    private var url:String? = null
    private var realm:String? = null
    private var clientId:String? = null
    private var username:String? = null
    private var password:String? = null
    private var poolSize:Int? = null
    private var poolStart:Boolean? = null
    private var readTimeout:Long? = null
    private var connectionTimeout:Long? = null;

    @Bean
    fun getKeycloak():Keycloak{
        LOGGER.info("[KEYCLOAK-ADMIN-CONFIGURATION] url: {}", url);
        LOGGER.info("[KEYCLOAK-ADMIN-CONFIGURATION] realm: {}", realm);
        LOGGER.info("[KEYCLOAK-ADMIN-CONFIGURATION] clientId: {}", clientId);
        LOGGER.info("[KEYCLOAK-ADMIN-CONFIGURATION] username: {}", username);
        LOGGER.info("[KEYCLOAK-ADMIN-CONFIGURATION] poolSize: {}", poolSize);
        LOGGER.info("[KEYCLOAK-ADMIN-CONFIGURATION] poolStart: {}", poolStart);

        var keycloak:Keycloak = KeycloakBuilder.builder()
                        .serverUrl(url)
                        .realm(realm)
                        .grantType(OAuth2Constants.PASSWORD)
                        .username(username)
                        .password(password)
                        .clientId(clientId)
                        .resteasyClient(
                            ResteasyClientBuilder()
                                .connectionPoolSize(poolSize!!)
                                .connectTimeout(connectionTimeout!!, TimeUnit.MILLISECONDS)
                                .readTimeout(readTimeout!!, TimeUnit.MILLISECONDS)
                                .build())
                        .build()

        if(poolStart!!){
            try{
                LOGGER.info("[KEYCLOAK-ADMIN-CONFIGURATION] starting ");
                var token:AccessTokenResponse = keycloak.tokenManager().grantToken()
                LOGGER.trace("[KEYCLOAK-ADMIN-CONFIGURATION] token type: {}", token.getTokenType());
                LOGGER.trace("[KEYCLOAK-ADMIN-CONFIGURATION] token: {}", token.getToken());
                LOGGER.info(
                    "[KEYCLOAK-ADMIN-CONFIGURATION] token expires in: {}", token.getRefreshExpiresIn());
                LOGGER.trace("[KEYCLOAK-ADMIN-CONFIGURATION] token scope: {}", token.getScope());
                LOGGER.trace("[KEYCLOAK-ADMIN-CONFIGURATION] refresh token: {}", token.getRefreshToken());
                LOGGER.trace("[KEYCLOAK-ADMIN-CONFIGURATION] session state: {}", token.getSessionState());
            } catch (e: NotAuthorizedException) {
                LOGGER.error("[KEYCLOAK-ADMIN-CONFIGURATION] erro ao obter token: {}", e.message);
                LOGGER.error("[KEYCLOAK-ADMIN-CONFIGURATION] stack trace error: ", e);
            }
        }

        return keycloak
    }

    fun getUrl(): String? {
        return url
    }

    fun setUrl(url: String?) {
        this.url = url
    }

    fun getRealm(): String? {
        return realm
    }

    fun setRealm(realm: String?) {
        this.realm = realm
    }

    fun getClientId(): String? {
        return clientId
    }

    fun setClientId(clientId: String?) {
        this.clientId = clientId
    }

    fun getUsername(): String? {
        return username
    }

    fun setUsername(username: String?) {
        this.username = username
    }

    fun getPassword(): String? {
        return password
    }

    fun setPassword(password: String?) {
        this.password = password
    }

    fun getPoolSize(): Int? {
        return poolSize
    }

    fun setPoolSize(poolSize: Int?) {
        this.poolSize = poolSize
    }

    fun isPoolStart(): Boolean {
        return poolStart!!
    }

    fun setPoolStart(poolStart: Boolean) {
        this.poolStart = poolStart
    }

    fun getReadTimeout(): Long? {
        return readTimeout
    }

    fun setReadTimeout(readTimeout: Int) {
        this.readTimeout = readTimeout.toLong()
    }

    fun getConnectionTimeout(): Long? {
        return connectionTimeout
    }

    fun setConnectionTimeout(connectionTimeout: Int) {
        this.connectionTimeout = connectionTimeout.toLong()
    }
}