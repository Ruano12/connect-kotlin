package com.connect.connect.login

import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.resource.RealmResource
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class KeycloakRealmRepository(val keycloak: Keycloak) {

    private val LOGGER:Logger = LoggerFactory.getLogger(KeycloakRealmRepository::class.java)

    fun getRealmResource():RealmResource = getRealmResource("keycloak-estudo")

    fun getRealmResource(realm:String): RealmResource {
        LOGGER.info("REALM-REPOSITORY-IMPL] getRealmResource: {}.", realm);
        var realmResource:RealmResource = keycloak.realm(realm)
        LOGGER.trace("[REALM-REPOSITORY-IMPL] realmResource: {}.", realmResource)
        return realmResource
    }
}