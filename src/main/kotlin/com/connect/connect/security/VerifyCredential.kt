package com.connect.connect.security

import com.connect.connect.login.LoginProcessor
import com.connect.connect.login.exceptions.InvalidCredentialsException
import com.connect.connect.security.exceptions.TechnicalException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

import java.net.URI;
import javax.xml.ws.Response

@Component
class VerifyCredential(val rest:RestTemplate) {

    private val LOGGER: Logger = LoggerFactory.getLogger(VerifyCredential::class.java)

    @Value("\${keycloak.auth-server-url}")
    private val authServerUrl: String? = null;

    @Value("\${keycloak.realm}")
    private val realm: String? = null

    @Value("\${login.external-login-resource}")
    private val resorce: String? = null

    private var INVALID_USER_CREDENTIALS:String = "Invalid user credentials"

    private var INVALID_GRANT:String = "invalid_grant"

    fun validateCredentials(username:String, password:String){
        var authbody:MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()

        authbody.add("grant_type", "password");
        authbody.add("client_id", "login-app");
        authbody.add("username", username);
        authbody.add("password",password);

        var requestToken:RequestEntity<*> = RequestEntity.post(
                URI.create(authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token"))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .accept(MediaType.APPLICATION_JSON)
            .body(authbody, String.javaClass)

        var respToken:ResponseEntity<*> = rest.exchange(requestToken, Map::class.java)

        if(respToken.statusCode.isError){
            LOGGER.warn("Keycloak retornou erro no login")
            var body:Map<*, *> = respToken.body as Map<*, *>
            if (INVALID_GRANT.equals(body.get("error"))
                    && INVALID_USER_CREDENTIALS.equals(body.get("error_description"))) {
                throw InvalidCredentialsException("Login e senha invalido")
            } else {
                throw TechnicalException(body.get("error_description") as String)
            }
        }
    }
}