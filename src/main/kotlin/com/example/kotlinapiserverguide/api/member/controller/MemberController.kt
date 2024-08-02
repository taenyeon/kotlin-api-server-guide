package com.example.kotlinapiserverguide.api.member.controller

import com.example.kotlinapiserverguide.api.member.domain.dto.MemberDto
import com.example.kotlinapiserverguide.api.member.service.MemberService
import com.example.kotlinapiserverguide.common.function.logger
import com.example.kotlinapiserverguide.common.http.constant.ResponseCode
import com.example.kotlinapiserverguide.common.http.domain.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/member")
class MemberController(private val memberService: MemberService) {
    val log = logger()

    @GetMapping("/{username}")
    fun findUser(@PathVariable username: String): ApiResponse<MemberDto> {
        val member = memberService.findMemberDto(username)
        return ApiResponse(member)
    }

}