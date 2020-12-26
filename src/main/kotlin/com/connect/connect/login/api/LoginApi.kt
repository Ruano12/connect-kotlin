package com.connect.connect.login.api

import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

interface LoginApi {

    @RequestMapping(value = ["/login"], method = [RequestMethod.POST], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun login(@RequestParam(value = "username") username:String, @RequestParam(value = "password") password:String):HttpEntity<Map<*, *>>?
}