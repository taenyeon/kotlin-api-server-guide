package com.example.kotlinapiserverguide.api.member.service

import com.example.kotlinapiserverguide.api.member.domain.entity.Member
import com.example.kotlinapiserverguide.api.member.repository.MemberRepository
import com.example.kotlinapiserverguide.common.exception.ResponseException
import com.example.kotlinapiserverguide.common.http.constant.ResponseCode
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class MemberCacheService(
    private val memberRepository: MemberRepository
) {

    @Cacheable(value = ["member"], key = "#id")
    fun findMember(id: Long): Member {
        return memberRepository.findById(id)
            .orElseThrow { ResponseException(ResponseCode.NOT_FOUND_ERROR) }
    }
}