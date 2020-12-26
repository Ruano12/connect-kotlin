package com.connect.connect.security

import com.connect.connect.security.exceptions.InvalidAccessException
import org.keycloak.KeycloakPrincipal
import org.keycloak.adapters.RefreshableKeycloakSecurityContext
import org.keycloak.adapters.springsecurity.account.KeycloakRole
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper
import org.springframework.stereotype.Component
import java.util.*
import java.util.Collection
import java.util.Map
import kotlin.collections.ArrayList
import kotlin.jvm.Throws

@Component
class UserInfoKeycloakAuthenticationProvider(val cachedUserInfoProvider: UserInfoOperations) : AuthenticationProvider {

    private val logger: Logger = LoggerFactory.getLogger(MyAccessDeniedHandler::class.java)

    private lateinit var grantedAuthoritiesMapper: GrantedAuthoritiesMapper;

    fun setGrantedAuthoritiesMapper(grantedAuthoritiesMapper:GrantedAuthoritiesMapper){
        this.grantedAuthoritiesMapper = grantedAuthoritiesMapper;
    }

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication {
        var  token = authentication as KeycloakAuthenticationToken
        var grantedAuthorities:ArrayList<GrantedAuthority> = ArrayList<GrantedAuthority>()

        addTokenRoles(token, grantedAuthorities )
        addUserInfoRoles(token, grantedAuthorities)

        return KeycloakAuthenticationToken(token.account,
                token.isInteractive, mapAuthorities(grantedAuthorities) )
    }

    private fun addTokenRoles(token: KeycloakAuthenticationToken, grantedAuthorities:ArrayList<GrantedAuthority>){
        for (role: String in token.account.roles){
            grantedAuthorities.add(KeycloakRole(role))
        }
    }

    private fun addUserInfoRoles(token: KeycloakAuthenticationToken, grantedAuthorities:ArrayList<GrantedAuthority>){
        var principal: KeycloakPrincipal<RefreshableKeycloakSecurityContext> = token.principal as KeycloakPrincipal<RefreshableKeycloakSecurityContext>
        var idToken:String = principal.keycloakSecurityContext.tokenString
        var resourceName:String = principal.keycloakSecurityContext.deployment.resourceName

        logger.debug("Obtendo userinfo do cache...")
        var node:Map<*, *>?

        try {
            node = cachedUserInfoProvider.get(idToken)
        }catch (e:RuntimeException){
            logger.error("Não foi possível obter dados do token no REDIS.", e)
            throw InvalidAccessException(e)
        }

        if(Objects.nonNull(node)){
            if(node!!.containsKey("resources")){
                node = node.get("resources") as Map<*, *>
                if(node.containsKey(resourceName)){
                    node = node.get(resourceName) as Map<*, *>
                    if(node.containsKey("roles")){
                        var roles:Array<String>
                        if (node.get("roles") != null && node.get("roles").javaClass.isArray()) {
                            roles = node.get("roles") as Array<String>
                        } else if (Objects.nonNull(node.get("roles")) &&
                                node.get("roles") is Collection<*>){
                            var colNodes:Collection<*>  = node.get("roles") as Collection<*>
                            roles = colNodes.toArray(arrayOfNulls<String>(colNodes.size())) as Array<String>
                        }else {
                            roles = arrayOf(node["roles"] as String)
                        }

                        logger.debug("Adicionando {} roles do UserInfo...", roles.size)
                        for(role:String in roles){
                            grantedAuthorities.add(KeycloakRole(role))
                        }
                    }
                }
            }
        }
    }

    private fun mapAuthorities(
            authorities: kotlin.collections.Collection<GrantedAuthority>): kotlin.collections.Collection<GrantedAuthority> {
        return if (grantedAuthoritiesMapper != null) grantedAuthoritiesMapper.mapAuthorities(authorities) else authorities
    }

    override fun supports(aClass: Class<*>?): Boolean {
        return KeycloakAuthenticationToken::class.java.isAssignableFrom(aClass)
    }
}