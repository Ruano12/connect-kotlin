package com.connect.connect.login

import org.springframework.stereotype.Component
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value
import java.util.*
import com.connect.connect.login.exceptions.UserNotFoundException

import org.apache.tomcat.jni.User.username
import org.keycloak.admin.client.resource.UserResource


@Component
class KeycloakUserBrowser(val userRepository: KeycloakUserRepository) {

    @Value("\${keycloak.realm}")
    private val realm:String? = null

    fun findById(@NotNull(message = "id obrigatorio") id:String): UserRepresentation =
        Optional.of(userRepository.getUsersResource(realm!!).get(id).toRepresentation())
            .orElse(null)

    fun findByUsername(@NotEmpty(message = "Informe o usuário") username:String):
            UserRepresentation = userRepository
                                        .getUsersResource(realm!!)
                                        .search(username)
                                        .stream()
                                        .findFirst()
                                        .orElseThrow { UserNotFoundException("Usuário não encontrado") }

    fun findUserResourceById(@NotNull(message = "id obrigatorio") id:String) :
            UserResource = Optional.of(userRepository.getUsersResource(realm!!).get(id))
                                    .orElse(null)






}