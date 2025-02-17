package com.example.kotlinapiserverguide.api.user.domain.dto

import au.com.console.kassava.kotlinEquals
import au.com.console.kassava.kotlinHashCode
import au.com.console.kassava.kotlinToString

class LoginRequest {
    lateinit var username: String
    lateinit var password: String

    override fun toString() = kotlinToString(properties = toStringProperties)
    override fun equals(other: Any?) = kotlinEquals(other = other, properties = equalsAndHashCodeProperties)
    override fun hashCode() = kotlinHashCode(properties = equalsAndHashCodeProperties)

    companion object {
        private val equalsAndHashCodeProperties = arrayOf(LoginRequest::username)
        private val toStringProperties = arrayOf(
            LoginRequest::username
        )
    }

}