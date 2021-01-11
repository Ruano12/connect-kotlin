package com.connect.connect.user.api.v1.browser.impl

import com.connect.connect.user.api.v1.browser.UserBrowserApi
import com.connect.connect.user.api.v1.browser.dto.FindPersonIdResponseDto
import com.connect.connect.user.browser.UserBrowser
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class UserBrowserApiImpl(val userBrowser: UserBrowser) : UserBrowserApi {

    override fun getLoggedPersonId(): HttpEntity<FindPersonIdResponseDto> {
        var personId:UUID? = userBrowser.getPersonId()

        return ResponseEntity<FindPersonIdResponseDto>(FindPersonIdResponseDto(personId!!), HttpStatus.OK)
    }
}