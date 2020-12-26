package com.connect.connect.login

import org.springframework.http.HttpStatus

class LoginProcessorResponse {

    private var status:HttpStatus? = null

    private var body:Map<*, *>? = null

    constructor(body:Map<*, *>?, status:HttpStatus ){
        this.body = body
        this.status = status;
    }

    fun getStatus(): HttpStatus? = this.status

    fun setStatus(status:HttpStatus?){
        this.status = status
    }

    fun getBody():Map<*, *>? = this.body

    fun setStatus(body:Map<*, *>){
        this.body = body
    }
}