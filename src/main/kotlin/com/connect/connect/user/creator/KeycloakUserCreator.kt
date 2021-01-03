package com.connect.connect.user.creator

import com.connect.connect.user.creator.bo.CreatorUserBo
import org.keycloak.representations.idm.UserRepresentation;

interface KeycloakUserCreator {

    fun createUser(bo: CreatorUserBo, sourceChannel:String?):UserRepresentation
}