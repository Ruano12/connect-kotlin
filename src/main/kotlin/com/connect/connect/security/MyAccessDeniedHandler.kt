package com.connect.connect.security

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class MyAccessDeniedHandler : AccessDeniedHandler {

    private val logger: Logger = LoggerFactory.getLogger(MyAccessDeniedHandler::class.java)

    override fun handle(request: HttpServletRequest, response: HttpServletResponse, accessDeniedException: AccessDeniedException?) {
        val auth: Authentication = SecurityContextHolder.getContext().authentication;
        logger.info("-------------------------------------------");

        if(Objects.nonNull(auth)){
            logger.info("User '"
                    + auth.name
                    + "' attempted to access the protected URL: "
                    + request.requestURI);
        }

        response.sendRedirect(request.contextPath+"/403");
    }
}