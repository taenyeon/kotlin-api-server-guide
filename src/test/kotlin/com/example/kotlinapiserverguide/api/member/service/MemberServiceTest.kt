package com.example.kotlinapiserverguide.api.member.service

import com.example.kotlinapiserverguide.api.member.domain.entity.Member
import com.example.kotlinapiserverguide.api.member.repository.MemberRepository
import com.example.kotlinapiserverguide.common.exception.ResponseException
import com.example.kotlinapiserverguide.common.function.encrypt
import com.example.kotlinapiserverguide.common.http.constant.ResponseCode
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MemberServiceTest {
    private val memberRepository: MemberRepository = mockk()
    private val memberService: MemberService = MemberService(memberRepository)

    private var username = "test"
    private var member = Member(
        1,
        "test",
        "test",
        "test",
        "01011111111",
        null
    )

    @Test
    fun addMember() {
    }

    @Test
    fun deleteMember() {
    }

    @Test
    fun findMember() {
    }

    @Test
    fun test_checkExistMember_exist() {
        every { memberRepository.findByUsername(username.encrypt()) } returns member
        val exception = assertThrows<ResponseException> {
            memberService.checkExistMember(username)
        }
        assertThat(ResponseCode.EXIST_MEMBER).isEqualTo(exception.responseCode)

    }

    @Test
    fun test_checkExistMember_not_exist() {
        every { memberRepository.findByUsername(username.encrypt()) } returns null
        memberService.checkExistMember(username)
        verify { memberRepository.findByUsername(username.encrypt()) }
    }

    @Test
    fun testFindMember() {
    }

    @Test
    fun findMemberDto() {
    }

    @Test
    fun testFindMemberDto() {
    }
}