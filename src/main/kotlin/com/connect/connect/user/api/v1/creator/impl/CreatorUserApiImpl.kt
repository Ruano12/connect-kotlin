package com.connect.connect.user.api.v1.creator.impl

import com.connect.connect.user.api.v1.creator.CreatorUserApi
import com.connect.connect.user.api.v1.creator.dto.CreatorUserRequestDto
import com.connect.connect.user.api.v1.creator.dto.UserCreatorResponseDto
import com.connect.connect.user.creator.UserCreator
import com.connect.connect.user.creator.bo.CreatorUserBo
import com.connect.connect.user.creator.bo.UserCreatorResponse
import com.connect.connect.user.creator.impl.UserCreatorImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class CreatorUserApiImpl(val userCreator: UserCreator) : CreatorUserApi {
    private val LOGGER: Logger = LoggerFactory.getLogger(UserCreatorImpl::class.java)

    override fun createUser(user: CreatorUserRequestDto): HttpEntity<UserCreatorResponseDto> {
        LOGGER.info("[CREATOR-USER-API-IMPL] Criando novo usuário no keycloak {}.", user.personId)

        var response:UserCreatorResponse =
            userCreator.createUser(CreatorUserBo(user))

        return ResponseEntity<UserCreatorResponseDto>(UserCreatorResponseDto(response), HttpStatus.OK)
    }
}