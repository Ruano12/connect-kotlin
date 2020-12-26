package com.connect.connect.password.impl

import com.connect.connect.login.KeycloakUserBrowser
import com.connect.connect.login.LoginProcessor
import com.connect.connect.password.PasswordUtils
import com.connect.connect.security.SecurityPrincipalHelper
import com.connect.connect.security.UserRequiredActions
import com.connect.connect.security.VerifyCredential
import org.keycloak.admin.client.resource.UserResource
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

@Component
class PasswordUtilsImpl(val userBrowser: KeycloakUserBrowser,
                        val securityPrincipalHelper: SecurityPrincipalHelper,
                        val verifyCredential: VerifyCredential) : PasswordUtils {

    private val LOGGER: Logger = LoggerFactory.getLogger(LoginProcessor::class.java)

    override fun resetPassword(userId: String, password: String) {
        var userResource:UserResource = userBrowser.findUserResourceById(userId)
        var passwordCred: CredentialRepresentation = CredentialRepresentation()
        passwordCred.isTemporary=false
        passwordCred.type = CredentialRepresentation.PASSWORD
        passwordCred.value = password

        try{
            userResource.resetPassword(passwordCred)
        } catch (e:Exception) {
            LOGGER.error("[PASSWORD UTILS] Resetando password {}", userId)
        }

        var userRepresentation: UserRepresentation = userResource.toRepresentation()
        var requiredActions:MutableList<String> = userRepresentation.requiredActions

        if(Objects.nonNull(requiredActions) && !requiredActions.isEmpty()){
            requiredActions.remove(UserRequiredActions.VERIFY_EMAIL.name)
            requiredActions.remove(UserRequiredActions.UPDATE_PASSWORD.name)
            userRepresentation.requiredActions = requiredActions
        }

        userRepresentation.isEmailVerified = true
        userResource.update(userRepresentation)
    }

    override fun changePassword(oldPassword: String, newPassword: String) {
        LOGGER.error("[PASSWORD UTILS] Alterando password")
        var id:String = securityPrincipalHelper.getUsername()

        var userResource:UserResource = userBrowser.findUserResourceById(id)

        var credentials:CredentialRepresentation = userResource.credentials().stream()
                            .findFirst()
                            .orElse(null)

        var username:String = userResource.toRepresentation().username
        verifyCredential.validateCredentials(username, oldPassword)

        this.resetPassword(id, newPassword)
    }
}