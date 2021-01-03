package com.connect.connect.user.creator.bo

import com.fasterxml.jackson.annotation.JsonProperty
import org.keycloak.representations.idm.UserRepresentation
import java.util.*

class UserCreatorResponse {

    var userId:String? = null
    var lastName: String? = null
    var enabled:Boolean? = null
    var username: String? = null
    var firstName: String? = null
    var email: String? = null
    var personId: UUID? = null

    constructor(representation: UserRepresentation){
        this.enabled = representation.isEnabled
        this.username = representation.username
        this.firstName = representation.firstName
        this.lastName = representation.lastName
        this.email = representation.email
        this.userId = representation.id
    }
}