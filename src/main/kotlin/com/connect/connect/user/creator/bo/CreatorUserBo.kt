package com.connect.connect.user.creator.bo

import com.connect.connect.user.api.v1.creator.dto.CreatorUserRequestDto
import java.util.*


class CreatorUserBo {

    var username: String? = null
    var password: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var email: String? = null
    var personId: UUID? = null
    var sourceName:String? = null

    constructor(dto:CreatorUserRequestDto){
        this.username = dto.username
        this.password = dto.password
        this.firstName = dto.firstName
        this.lastName = dto.lastName
        this.email = dto.email
        this.personId = dto.personId
    }
}