package com.connect.connect.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestApi {

    @GetMapping(value = ["/test"])
    fun getTest(): ResponseEntity<String> = ResponseEntity.ok("OK")
}