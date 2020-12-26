package com.connect.connect.login.api.impl

import com.connect.connect.login.LoginProcessor
import com.connect.connect.login.LoginProcessorResponse
import com.connect.connect.login.api.LoginApi
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import javax.transaction.Transactional

@RestController
@Transactional
class LoginApiImpl(val loginProcessor: LoginProcessor) : LoginApi {

    override fun login(username: String, password: String): HttpEntity<Map<*, *>>? {
        var lps:LoginProcessorResponse =
            loginProcessor.validateLogin(username, password)

        return ResponseEntity<Map<*, *>>(lps.getBody(), lps.getStatus()!!)
    }
}