package com.example.kotlinapiserverguide.serviceTest.member.coverage.memberService

import com.example.kotlinapiserverguide.api.member.domain.entity.Member
import com.example.kotlinapiserverguide.api.member.repository.MemberRepository
import com.example.kotlinapiserverguide.api.member.service.MemberService
import com.example.kotlinapiserverguide.common.exception.ResponseException
import com.example.kotlinapiserverguide.common.function.encrypt
import com.example.kotlinapiserverguide.common.http.constant.ResponseCode
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime

internal class FindMemberDtoByIdTest : DescribeSpec({

    member.createdAt = LocalDateTime.now()
    member.updatedAt = LocalDateTime.now()

    describe("회원 DTO 조회 (id)") {

        context("기존 회원 정보가 없으면") {

            every { memberRepository.findByIdOrNull(id) } returns null

            it("[ERROR] NOT_FOUND") {

                shouldThrow<ResponseException> { memberService.findMemberDto(id) }
                    .responseCode shouldBe ResponseCode.NOT_FOUND_ERROR
            }
        }

        context("회원 정보가 있으면") {

            every { memberRepository.findByIdOrNull(id) } returns member

            it("SUCCESS") {

                memberService.findMemberDto(id).id shouldBe id
            }
        }
    }
}) {
    companion object {

        private val memberRepository: MemberRepository = mockk()

        private val memberService: MemberService = MemberService(memberRepository)

        private var id = 1L

        private var username = "test"

        private var member = Member(
            1,
            "test".encrypt(),
            "test",
            "test".encrypt(),
            "01011111111".encrypt(),
            null
        )
    }
}