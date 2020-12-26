package com.connect.connect.login

import org.keycloak.admin.client.resource.UsersResource
import org.springframework.stereotype.Component

@Component
class KeycloakUserRepository(val realmRepository: KeycloakRealmRepository) {

    fun getUsersResource(realm:String): UsersResource = realmRepository.getRealmResource(realm).users()
}