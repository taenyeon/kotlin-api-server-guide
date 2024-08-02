package com.example.kotlinapiserverguide.api.user.domain.mapper

import com.example.kotlinapiserverguide.api.member.domain.entity.Member
import com.example.kotlinapiserverguide.api.user.domain.dto.JoinRequest
import com.example.kotlinapiserverguide.common.encrypt.annotation.Encrypt
import com.example.kotlinapiserverguide.common.encrypt.annotation.PasswordEncrypt
import com.example.kotlinapiserverguide.common.interfaces.EntityMapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import com.example.kotlinapiserverguide.common.util.EncryptUtil

@Mapper(uses = [EncryptUtil::class])
interface JoinRequestMapper : EntityMapper<Member, JoinRequest> {

    @Mapping(source = "password", target = "password", qualifiedBy = [PasswordEncrypt::class])
    @Mapping(source = "username", target = "username", qualifiedBy = [Encrypt::class])
    @Mapping(source = "name", target = "name", qualifiedBy = [Encrypt::class])
    @Mapping(source = "phoneNumber", target = "phoneNumber", qualifiedBy = [Encrypt::class])
    override fun toEntity(dto: JoinRequest): Member

    override fun toDto(entity: Member): JoinRequest
}