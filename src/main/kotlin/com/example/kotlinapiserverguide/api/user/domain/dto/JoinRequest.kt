package com.example.kotlinapiserverguide.api.user.domain.dto

import au.com.console.kassava.kotlinEquals
import au.com.console.kassava.kotlinHashCode
import au.com.console.kassava.kotlinToString
import jakarta.validation.constraints.NotNull

class JoinRequest{
    @NotNull
    lateinit var username: String
    @NotNull
    lateinit var password: String
    @NotNull
    lateinit var name: String
    @NotNull
    lateinit var phoneNumber: String

    override fun toString() = kotlinToString(properties = toStringProperties)
    override fun equals(other: Any?) = kotlinEquals(other = other, properties = equalsAndHashCodeProperties)
    override fun hashCode() = kotlinHashCode(properties = equalsAndHashCodeProperties)

    companion object {
        private val equalsAndHashCodeProperties = arrayOf(JoinRequest::username)
        private val toStringProperties = arrayOf(
            JoinRequest::username,
            JoinRequest::name
        )
    }
}