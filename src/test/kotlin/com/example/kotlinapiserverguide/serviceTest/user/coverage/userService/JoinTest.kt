package com.example.kotlinapiserverguide.serviceTest.user.coverage.userService

import com.example.kotlinapiserverguide.api.member.domain.entity.Member
import com.example.kotlinapiserverguide.api.member.service.MemberService
import com.example.kotlinapiserverguide.api.user.domain.dto.JoinRequest
import com.example.kotlinapiserverguide.api.user.service.UserService
import com.example.kotlinapiserverguide.common.exception.ResponseException
import com.example.kotlinapiserverguide.common.http.constant.ResponseCode
import com.example.kotlinapiserverguide.common.security.provider.JwtTokenProvider
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

internal class JoinTest : DescribeSpec({

    describe("유저 생성") {
        context("생성 성공") {
            every { memberService.checkExistMember("test") } returns Unit
            every { memberService.addMember(Member(null, "test", "test", "test", "01011111111", null)) } returns 1
            val joinRequest = JoinRequest()
            joinRequest.username = "test"
            joinRequest.name = "test"
            joinRequest.password = "test"
            joinRequest.phoneNumber = "01011111111"

            it("SUCCESS") {
                userService.join(joinRequest).id shouldBe 1
            }
        }
        context("이미 같은 username의 회원이 존재한다면") {

            every { memberService.checkExistMember("test") } throws (ResponseException(ResponseCode.EXIST_MEMBER))

            val joinRequest = JoinRequest()
            joinRequest.username = "test"
            joinRequest.name = "test"
            joinRequest.password = "test"
            joinRequest.phoneNumber = "01011111111"

            it("[ERROR] EXIST_MEMBER_ERROR") {
                shouldThrow<ResponseException> { userService.join(joinRequest) }
                    .responseCode shouldBe ResponseCode.EXIST_MEMBER
            }
        }
    }


}) {
    companion object {
        private val memberService: MemberService = mockk()
        private val jwtTokenProvider: JwtTokenProvider = mockk()
        private val userService = UserService(memberService, BCryptPasswordEncoder(), jwtTokenProvider)
    }
}