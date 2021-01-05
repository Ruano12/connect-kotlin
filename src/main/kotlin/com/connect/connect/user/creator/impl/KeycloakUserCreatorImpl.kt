package com.connect.connect.user.creator.impl

import com.connect.connect.login.KeycloakRealmRepository
import com.connect.connect.user.creator.KeycloakUserCreator
import com.connect.connect.user.creator.RealmNotFoundException
import com.connect.connect.user.creator.UserAlreadyExistException
import com.connect.connect.user.creator.bo.CreatorUserBo
import com.connect.connect.util.JsonUtil
import org.keycloak.admin.client.resource.RealmResource
import org.keycloak.admin.client.resource.UsersResource
import org.keycloak.representations.idm.UserRepresentation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import javax.ws.rs.core.Response;

@Component
class KeycloakUserCreatorImpl(val realmRepository: KeycloakRealmRepository) : KeycloakUserCreator {

    private val LOGGER: Logger = LoggerFactory.getLogger(KeycloakUserCreatorImpl::class.java)

    @Value("required.actions")
    var requiredActions:List<String> = ArrayList<String>();

    override fun createUser(bo: CreatorUserBo, sourceChannel: String?): UserRepresentation {
        LOGGER.info("[KEYCLOAK-USER-CREATOR-IMPL] new user {}.", bo.username);

        var user:UserRepresentation = UserRepresentation()
        user.isEnabled = true
        user.isEmailVerified = true
        user.username = bo.username
        user.firstName = bo.firstName
        user.lastName = bo.lastName
        user.email = bo.email

        user.requiredActions = requiredActions

        user.singleAttribute("person-id", bo.personId.toString())

        var realmResource:RealmResource = realmRepository.getRealmResource()
        var usersResource:UsersResource = realmResource.users()

        var response:Response = usersResource.create(user)
        var httpsStatus:HttpStatus = HttpStatus.valueOf(response.status)
        if(httpsStatus.is2xxSuccessful){
            var userId:String = response.location.path.replace(".*/([^/]+)$".toRegex(), "$1")
            LOGGER.info("[KEYCLOAK-USER-CREATOR-IMPL] usuario criado: {}", userId)
            user.id = userId

        } else if (httpsStatus.equals(HttpStatus.CONFLICT)){
            throw UserAlreadyExistException("Usuário ja cadastrado");
        } else if (httpsStatus.equals(HttpStatus.NOT_FOUND)){
            var message:String = JsonUtil.jsonToJsonNode(response.readEntity(String::class.java)).get("error").asText()
            LOGGER.error("[KEYCLOAK-USER-CREATOR-IMPL] Error ao criar usuário {}", message)
            throw RealmNotFoundException("Erro interno")
        }

        return user
    }
}