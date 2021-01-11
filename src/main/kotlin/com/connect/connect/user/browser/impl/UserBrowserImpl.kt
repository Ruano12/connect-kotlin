package com.connect.connect.user.browser.impl

import com.connect.connect.security.SecurityPrincipalHelper
import com.connect.connect.user.browser.UserBrowser
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserBrowserImpl(val securityPrincipalHelper: SecurityPrincipalHelper) : UserBrowser {

    override fun getPersonId(): UUID {
        var user:Optional<UUID>? = securityPrincipalHelper.getPersonId()
        return user!!.orElse(null)
    }
}