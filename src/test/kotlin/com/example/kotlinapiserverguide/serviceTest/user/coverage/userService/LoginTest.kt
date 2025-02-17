package com.example.kotlinapiserverguide.serviceTest.user.coverage.userService

import com.example.kotlinapiserverguide.api.member.domain.entity.Member
import com.example.kotlinapiserverguide.api.member.service.MemberService
import com.example.kotlinapiserverguide.api.user.domain.dto.JwtToken
import com.example.kotlinapiserverguide.api.user.domain.dto.LoginRequest
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

internal class LoginTest : DescribeSpec({
    describe("유저 조회 (로그인 상태)") {
        context("DB에 해당하는 username이 없으면") {

            every { memberService.findMember("123") } throws ResponseException(ResponseCode.NOT_FOUND_ERROR)

            it("[ERROR] NOT_FOUND_ERROR") {
                val loginRequest = LoginRequest()
                loginRequest.username = "123"
                loginRequest.password = "test"

                shouldThrow<ResponseException> { userService.login(loginRequest) }
                    .responseCode shouldBe ResponseCode.NOT_FOUND_ERROR
            }

        }
        context("비밀번호가 일치하지 않으면") {
            every { memberService.findMember("test") } returns member

            it("[ERROR] WRONG_PASSWORD_ERROR") {
                val loginRequest = LoginRequest()
                loginRequest.username = "test"
                loginRequest.password = "1234"
                shouldThrow<ResponseException> { userService.login(loginRequest) }
                    .responseCode shouldBe ResponseCode.WRONG_PASSWORD_ERROR
            }
        }
        context("로그인에 성공하면") {
            every { memberService.findMember("test") } returns member
            every { jwtTokenProvider.generateToken(1) } returns JwtToken("test", "test")
            it("SUCCESS") {
                val loginRequest = LoginRequest()
                loginRequest.username = "test"
                loginRequest.password = "test"

                userService.login(loginRequest).accessToken shouldBe "test"
            }
        }
    }


}) {
    companion object {
        private val memberService: MemberService = mockk();
        private val jwtTokenProvider: JwtTokenProvider = mockk()
        private val userService = UserService(memberService, BCryptPasswordEncoder(), jwtTokenProvider);

        private var member = Member(
            1,
            "test",
            "\$2a\$10\$TufC8ijEnWjw1TVp726LtugLZprXyCeaIYotDV8i2bKHpa.jmqNVO",
            "test",
            "01011111111",
            null
        )
    }
}