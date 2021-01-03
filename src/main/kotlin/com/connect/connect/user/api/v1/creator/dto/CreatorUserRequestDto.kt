package com.connect.connect.user.api.v1.creator.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.util.*
import javax.validation.constraints.NotEmpty

class CreatorUserRequestDto : Serializable {

    @JsonProperty("username")
    @NotEmpty(message = "Informe o username")
    var username: String? = null

    @JsonProperty(value = "password")
    @NotEmpty(message = "Informe o password")
    var password: String? = null

    @JsonProperty(value = "first-name")
    @NotEmpty(message = "informe o primeiro nome")
    var firstName: String? = null

    @JsonProperty(value = "last-name")
    @NotEmpty(message = "Informe o sobrenome")
    var lastName: String? = null

    @JsonProperty(value = "email")
    @NotEmpty(message = "Informe o email")
    var email: String? = null

    @JsonProperty(value = "person-id")
    @NotEmpty(message = "Informe o person-id")
    var personId:UUID? = null
}

