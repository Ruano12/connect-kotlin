package com.connect.connect.password

interface PasswordUtils {

    fun resetPassword(userId:String, password:String);

    fun changePassword(oldPassword:String, newPassword:String);
}