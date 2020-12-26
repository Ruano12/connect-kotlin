package com.connect.connect.password.api.impl

import com.connect.connect.password.PasswordUtils
import com.connect.connect.password.api.ChangePasswordApi
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import javax.transaction.Transactional

@RestController
@Transactional
class ChangePasswordApiImpl(val passwordUtils: PasswordUtils) : ChangePasswordApi {

    override fun changePasswrod(newPassword: String, oldPassword: String): HttpEntity<Void> {
        passwordUtils.changePassword(oldPassword, newPassword)

        return ResponseEntity<Void>( HttpStatus.OK)
    }
}