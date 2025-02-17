package com.example.kotlinapiserverguide.api.member.domain.dto

import java.time.LocalDateTime

class MemberDto(
    var id: Long?,
    var username: String,
    var name: String,
    var phoneNumber: String,
    var createdAt: LocalDateTime,
    var updatedAt: LocalDateTime,
    var imageUrl: String?,
) {

}