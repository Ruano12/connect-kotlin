package com.connect.connect.user.creator.impl

import com.connect.connect.user.creator.KeycloakUserCreator
import com.connect.connect.user.creator.UserCreator
import com.connect.connect.user.creator.bo.CreatorUserBo
import com.connect.connect.user.creator.bo.UserCreatorResponse
import org.keycloak.representations.idm.UserRepresentation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class UserCreatorImpl(val userCreator:KeycloakUserCreator): UserCreator {

    override fun createUser(bo: CreatorUserBo): UserCreatorResponse {
        var user:UserRepresentation = userCreator.createUser(bo, bo.sourceName)
        var userCreatorResponse:UserCreatorResponse = UserCreatorResponse(user)
        userCreatorResponse.personId =bo.personId
        return userCreatorResponse

    }

}