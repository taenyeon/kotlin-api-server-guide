package com.example.kotlinapiserverguide.api.user.domain.dto

import au.com.console.kassava.kotlinEquals
import au.com.console.kassava.kotlinHashCode
import au.com.console.kassava.kotlinToString

class JoinResponse(
    var id: Long
) {

    override fun toString() = kotlinToString(properties = toStringProperties)
    override fun equals(other: Any?) = kotlinEquals(other = other, properties = equalsAndHashCodeProperties)
    override fun hashCode() = kotlinHashCode(properties = equalsAndHashCodeProperties)

    companion object {
        private val equalsAndHashCodeProperties = arrayOf(JoinResponse::id)
        private val toStringProperties = arrayOf(
            JoinResponse::id,
        )
    }
}