package com.example.kotlinapiserverguide.api.member.service

import com.example.kotlinapiserverguide.api.member.domain.entity.Member
import com.example.kotlinapiserverguide.api.member.repository.MemberRepository
import com.example.kotlinapiserverguide.common.exception.ResponseException
import com.example.kotlinapiserverguide.common.function.decrypt
import com.example.kotlinapiserverguide.common.function.encrypt
import com.example.kotlinapiserverguide.common.http.constant.ResponseCode
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull

class MemberServiceTest {
    private val memberRepository: MemberRepository = mockk()
    private val memberService: MemberService = MemberService(memberRepository)

    private var id = 1L
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
        every {memberService.checkExistMember(username)} returns Unit

        val addMemberId = memberService.addMember(member)

        assertThat(id).isEqualTo(addMemberId)
    }

    @Test
    fun deleteMember() {

    }

    @Test
    @DisplayName("회원 조회 (id) - exist")
    fun test_findMember_by_id_exist() {
        every { memberRepository.findByIdOrNull(id) } returns member

        val findMember = memberService.findMember(id)

        assertThat(id).isEqualTo(findMember.id)
    }

    @Test
    @DisplayName("회원 조회 (id) - not_exist")
    fun test_findMember_by_id_not_exist() {
        every { memberRepository.findByIdOrNull(id) } returns null

        val exception = assertThrows<ResponseException> { memberService.findMember(id) }

        assertThat(ResponseCode.NOT_FOUND_ERROR).isEqualTo(exception.responseCode)
    }

    @Test
    @DisplayName("이미 로그인 id 있는지 체크 - 실패")
    fun test_checkExistMember_exist() {
        every { memberRepository.findByUsername(username.encrypt()) } returns member

        val exception = assertThrows<ResponseException> { memberService.checkExistMember(username) }

        assertThat(ResponseCode.EXIST_MEMBER).isEqualTo(exception.responseCode)
    }

    @Test
    @DisplayName("이미 로그인 id 있는지 체크 - 성공")
    fun test_checkExistMember_not_exist() {
        every { memberRepository.findByUsername(username.encrypt()) } returns null

        memberService.checkExistMember(username)
    }

    @Test
    @DisplayName("회원 조회 (username) - not_exist")
    fun test_findMember_by_username_exist() {
        every { memberRepository.findByUsername(username.encrypt()) } returns member

        val findMember = memberService.findMember(username)

        assertThat(username).isEqualTo(findMember.username?.decrypt())
    }

    @Test
    @DisplayName("회원 조회 (username) - not_exist")
    fun test_findMember_by_username_not_exist() {
        every { memberRepository.findByUsername(username.encrypt()) } returns null

        val exception = assertThrows<ResponseException> { memberService.findMember(username) }

        assertThat(ResponseCode.NOT_FOUND_ERROR).isEqualTo(exception.responseCode)
    }

    @Test
    fun findMemberDto() {

    }

    @Test
    fun testFindMemberDto() {

    }
}