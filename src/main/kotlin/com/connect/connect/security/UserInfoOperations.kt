package com.connect.connect.security

import java.util.Map;

import org.springframework.cache.annotation.Cacheable;

interface UserInfoOperations {

    @SuppressWarnings("rawtypes")
    @Cacheable(cacheManager = "userInfoCacheManager", value = ["auth-userinfo"], key = "#accessToken")
    open fun get(accessToken: String): Map<*, *>?
}
