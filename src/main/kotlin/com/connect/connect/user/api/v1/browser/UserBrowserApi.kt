package com.connect.connect.user.api.v1.browser

import com.connect.connect.user.api.v1.browser.dto.FindPersonIdResponseDto
import org.springframework.http.HttpEntity
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@RequestMapping("/v1")
interface UserBrowserApi {

    @RequestMapping(value = ["/users/find-logged-person-id"], method = [RequestMethod.GET], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getLoggedPersonId():HttpEntity<FindPersonIdResponseDto>
}