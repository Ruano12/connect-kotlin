package com.connect.connect.user.api.v1.creator.dto

import com.connect.connect.user.creator.bo.UserCreatorResponse
import com.fasterxml.jackson.annotation.JsonProperty
import org.keycloak.representations.idm.UserRepresentation
import java.util.*

class UserCreatorResponseDto {

    @JsonProperty(value = "user-id")
    var userId:String? = null

    @JsonProperty(value = "last-name")
    var lastName: String? = null

    @JsonProperty(value = "enabled")
    var enabled:Boolean? = null

    @JsonProperty("username")
    var username: String? = null

    @JsonProperty(value = "first-name")
    var firstName: String? = null

    @JsonProperty(value = "email")
    var email: String? = null

    @JsonProperty(value = "person-id")
    var personId: UUID? = null

    constructor(representation: UserCreatorResponse){
        this.enabled = representation.enabled
        this.username = representation.username
        this.firstName = representation.firstName
        this.lastName = representation.lastName
        this.email = representation.email
        this.userId = representation.userId
        this.personId = representation.personId
    }
}