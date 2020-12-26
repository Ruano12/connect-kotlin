package com.connect.connect.security

enum class KeyCloakCustomFields {
    PERSON_ID("person-id"),
    IS_APPLICATION("is-application");

    private var id:String? = null

    constructor(id:String) {
        this.id = id
    }

    fun getId():String? = this.id
}