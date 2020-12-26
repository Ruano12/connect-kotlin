package com.connect.connect.login

import org.keycloak.admin.client.resource.GroupsResource
import org.springframework.stereotype.Component

@Component
class KeycloakGroupRepository(val realmRepository: KeycloakRealmRepository) {

    fun getGroupResource() : GroupsResource = realmRepository.getRealmResource().groups();

}