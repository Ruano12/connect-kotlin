package com.connect.connect.user.api.v1.creator

import com.connect.connect.user.api.v1.creator.dto.CreatorUserRequestDto
import com.connect.connect.user.api.v1.creator.dto.UserCreatorResponseDto
import org.springframework.http.HttpEntity
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import javax.validation.Valid

@RequestMapping("/v1")
interface CreatorUserApi {

    @RequestMapping(value = ["/users"], method = [RequestMethod.POST], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createUser(@Valid @RequestBody user: CreatorUserRequestDto) : HttpEntity<UserCreatorResponseDto>
}