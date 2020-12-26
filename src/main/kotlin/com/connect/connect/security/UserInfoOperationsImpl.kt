package com.connect.connect.security
import java.util.Map;
import org.springframework.stereotype.Component

@Component
class UserInfoOperationsImpl : UserInfoOperations {

    override fun get(accessToken: String): Map<*, *>? {
       return null
    }
    //open override  fun get(accessToken: String):Map<*, *> = null
}
