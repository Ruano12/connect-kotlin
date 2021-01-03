package com.connect.connect.user.creator

import com.connect.connect.user.creator.bo.CreatorUserBo
import com.connect.connect.user.creator.bo.UserCreatorResponse
import javax.validation.constraints.NotNull

interface UserCreator {

    fun createUser(@NotNull bo:CreatorUserBo): UserCreatorResponse
}