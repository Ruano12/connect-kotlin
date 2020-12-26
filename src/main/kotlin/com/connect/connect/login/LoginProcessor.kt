package com.connect.connect.login

import com.connect.connect.login.exceptions.*
import org.keycloak.representations.idm.UserRepresentation
import java.net.URI;

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
import sun.misc.Request
import java.util.*

@Component
class LoginProcessor(val keycloakUserBrowser: KeycloakUserBrowser, val rest:RestTemplate) {

    private val LOGGER: Logger = LoggerFactory.getLogger(LoginProcessor::class.java)

    @Value("\${keycloak.auth-server-url}")
    private val authServerUrl: String? = null;

    @Value("\${keycloak.realm}")
    private val realm: String? = null

    @Value("\${login.external-login-resource}")
    private val resorce: String? = null

    fun validateLogin(username: String, password: String): LoginProcessorResponse {
        var authbody: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()

        authbody.add("grant_type", "password");
        authbody.add("client_id", "login-app");
        authbody.add("username", username);
        authbody.add("password", password);

        var lps: LoginProcessorResponse? = null

        try {
            LOGGER.info("Chamando operação de login no keycloak...");
            var requestToken: RequestEntity<*> = RequestEntity.post(
                URI.create(authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token")
            )
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(authbody, String.javaClass)

            var respToken: ResponseEntity<*> = rest.exchange(requestToken, Map::class.java)
            var accessToken: String? = null

            if (respToken.statusCode.isError) {
                LOGGER.warn("Keycloak retornou erro no login")
                tryConvertToException(respToken.body as Map<*, *>)
            }

            lps = LoginProcessorResponse(respToken.body as Map<*, *>, respToken.statusCode)
            accessToken = lps.getBody()!!.get("access_token") as String

            LOGGER.info("Chamando operação de userinfo no keycloak...");

            var requestUserInfo:RequestEntity<*> = RequestEntity.post(
                URI.create(
                    authServerUrl + "/realms/" + realm + "/protocol/openid-connect/userinfo"))
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+accessToken)
                .body("")

            var respUserInfo:ResponseEntity<*> = rest.exchange(requestUserInfo, Map::class.java)
            var mapUserInfo:Map<*, *> = respUserInfo.body as Map<*, *>

            if(respUserInfo.statusCode.isError){
                LOGGER.error("Keycloak retornou erro no userinfo");
                tryConvertToException(mapUserInfo);
            }

            if("password".equals(authbody.getFirst("grant_type"))) {

                var userId:String = mapUserInfo.get("sub") as String
                LOGGER.debug("Buscando informações do usuário {} no realm {}...", userId, realm);
                var userRepresentation: UserRepresentation = keycloakUserBrowser.findById(userId)

                if(Objects.isNull(userRepresentation)){
                    throw UsernameNotFoundException("Usuário não encontrado")
                }

                if(Objects.nonNull(userRepresentation.attributes)){
                    var category:List<String>? = userRepresentation.attributes.get("category")
                    if(Objects.nonNull(category) && !category.isNullOrEmpty()){
                        if(UserGroup.CUSTOMER.toString().equals(category.get(0))){
                            var customerStatus:List<String>? =
                                userRepresentation.attributes.get("customer-status")

                            if(Objects.nonNull(customerStatus) && !customerStatus.isNullOrEmpty()){
                                if(CustomerStatus.BLOCKED.toString()
                                        .equals(customerStatus.get(0).toString())){
                                    throw AccountDisabledException("Usuário bloqueado")
                                } else if(CustomerStatus.INACTIVE.toString()
                                        .equals(customerStatus.get(0).toString())){
                                    throw AccountDisabledException("Usuário inativo")
                                } else if (CustomerStatus.WAITING_APPROVAL.toString()
                                        .equals(customerStatus.get(0).toString())){
                                    throw LoginWaitingApprovalException("Usuário aguardando aprovação")
                                }
                            }else {
                                LOGGER.warn(
                                    "O usuário {} não possui atributo de customer-status, aceitando...", username);
                            }
                        }
                    } else {
                        LOGGER.warn(
                            "O usuário {} não possui atributo de category, aceitando...", username);
                    }
                }else {
                    LOGGER.warn("O usuário {} não possui atributos, aceitando...", username);
                }
            }

            LOGGER.info("Login realizado com sucesso...");

            return lps
        }catch ( e: Exception) {
            LOGGER.error("Erro ao conectar no keycloak", e);
            throw e;
        }
    }

    private fun tryConvertToException(body:Map<*, *>) {
        if("invalid_grant".equals(body.get("error"))
            && "Account is not fully set up".equals(body.get("error_description"))){
            throw ValidationEmailPendingException(body.get("error_description") as String)
        } else if ("invalid_grant".equals(body.get("error"))
            && "Invalid user credentials".equals(body.get("error_description"))) {
            throw InvalidCredentialsException(body.get("error_description") as String)
        } else if ("invalid_grant".equals(body.get("error"))
            && "Account disabled".equals(body.get("error_description"))) {
            throw AccountDisabledException(body.get("error_description") as String)
        } else {
            throw AccessDeniedException(body.get("error_description") as String)
        }
    }
}