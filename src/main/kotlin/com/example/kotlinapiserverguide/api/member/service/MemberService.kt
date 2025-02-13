package com.example.kotlinapiserverguide.api.member.service

import com.example.kotlinapiserverguide.api.member.domain.mapper.MemberDtoMapper
import com.example.kotlinapiserverguide.api.member.domain.dto.MemberDto
import com.example.kotlinapiserverguide.api.member.domain.entity.Member
import com.example.kotlinapiserverguide.api.member.repository.MemberRepository
import com.example.kotlinapiserverguide.common.exception.ResponseException
import com.example.kotlinapiserverguide.common.function.encrypt
import com.example.kotlinapiserverguide.common.function.logger
import com.example.kotlinapiserverguide.common.http.constant.ResponseCode
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service
import org.springframework.data.repository.findByIdOrNull

@Service
class MemberService(
    private val memberRepository: MemberRepository,
) {
    val log = logger()

    // mapStruct
    private val memberDtoMapper: MemberDtoMapper = Mappers.getMapper(MemberDtoMapper::class.java)

    // Entity
    fun addMember(member: Member): Long {
        checkExistMember(member.username!!)
        return memberRepository.save(member).id!!
    }

    fun deleteMember(id: Long) {
        try {
            memberRepository.deleteById(id)
        } catch (e: Exception) {
            throw ResponseException(ResponseCode.NOT_FOUND_ERROR, e)
        }
    }

    fun findMember(username: String): Member {
        return memberRepository.findByUsername(username.encrypt())
            ?: throw ResponseException(ResponseCode.NOT_FOUND_ERROR)
    }

    fun findMember(id: Long): Member {
        return memberRepository.findByIdOrNull(id)
            ?: throw ResponseException(ResponseCode.NOT_FOUND_ERROR)
    }

    // Dto
    fun findMemberDto(username: String): MemberDto {
        return memberDtoMapper.toDto(findMember(username))
    }

    fun findMemberDto(id: Long): MemberDto {
        return memberDtoMapper.toDto(findMember(id))
    }

    // Validate
    fun checkExistMember(username: String) {
        if (memberRepository.findByUsername(username.encrypt()) != null) {
            throw ResponseException(ResponseCode.EXIST_MEMBER)
        }
    }
}