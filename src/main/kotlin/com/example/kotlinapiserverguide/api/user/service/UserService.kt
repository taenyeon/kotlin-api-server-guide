package com.example.kotlinapiserverguide.api.user.service

import com.example.kotlinapiserverguide.common.security.provider.JwtTokenProvider
import com.example.kotlinapiserverguide.api.user.domain.mapper.UserMapper
import org.mapstruct.factory.Mappers
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import com.example.kotlinapiserverguide.api.user.domain.mapper.JoinRequestMapper
import com.example.kotlinapiserverguide.api.member.domain.dto.MemberDto
import com.example.kotlinapiserverguide.api.member.domain.entity.Member
import com.example.kotlinapiserverguide.api.member.service.MemberService
import com.example.kotlinapiserverguide.api.user.domain.dto.*
import com.example.kotlinapiserverguide.common.exception.ResponseException
import com.example.kotlinapiserverguide.common.http.constant.ResponseCode
import com.example.kotlinapiserverguide.common.security.constant.TokenStatus
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

@Service
class UserService(
    private val memberService: MemberService,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
) {
    private val userMapper: UserMapper = Mappers.getMapper(UserMapper::class.java)

    private val joinRequestMapper: JoinRequestMapper = Mappers.getMapper(JoinRequestMapper::class.java)

    fun getUser(): MemberDto {
        if (SecurityContextHolder.getContext().authentication.details == null)
            throw ResponseException(ResponseCode.INVALID_TOKEN)

        return userMapper.toMemberResponse(SecurityContextHolder.getContext().authentication.details as User)
    }

    fun join(joinRequest: JoinRequest): JoinResponse {
        memberService.checkExistMember(joinRequest.username)
        val member = joinRequestMapper.toEntity(joinRequest)
        return JoinResponse(memberService.addMember(member))
    }

    fun login(loginRequest: LoginRequest): JwtToken {
        val member: Member = memberService.findMember(loginRequest.username)
        matchingPassword(loginRequest.password, member.password!!)
        return jwtTokenProvider.generateToken(member.id!!)
    }

    fun logout(id: Long) {
        jwtTokenProvider.dropRefreshToken(id)
    }

    fun reIssueAccessToken(refreshToken: String): String {

        when (jwtTokenProvider.validateToken(refreshToken)) {
            TokenStatus.ALLOW -> {
                val id = jwtTokenProvider.parseIdFromJWT(refreshToken)
                return jwtTokenProvider.generateAccessToken(id, now = Date())
            }

            else -> throw ResponseException(ResponseCode.INVALID_TOKEN)
        }

    }

    fun matchingPassword(rawPassword: String, encodedPassword: String) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword))
            throw ResponseException(ResponseCode.WRONG_PASSWORD_ERROR)
    }
}