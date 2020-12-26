package com.connect.connect.password.api

import org.springframework.http.HttpEntity
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

interface ChangePasswordApi {

    @RequestMapping(value = ["/change-password"], method = [RequestMethod.PUT], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun changePasswrod(@RequestParam(value = "new-password") newPassword:String, @RequestParam(value = "old-password") oldPassword:String ) :
            HttpEntity<Void>
}