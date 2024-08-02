package com.example.kotlinapiserverguide.common.interfaces

interface EntityMapper<E, D> {

    fun toEntity(dto: D): E
    fun toDto(entity: E): D
}