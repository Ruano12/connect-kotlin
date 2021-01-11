package com.connect.connect.user.api.v1.browser.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

class FindPersonIdResponseDto {

    @JsonProperty(value = "person-id")
    var personId: UUID? = null

    constructor(personId:UUID){
        this.personId = personId
    }
}