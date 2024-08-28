package com.example.kotlinapiserverguide.api

import com.example.kotlinapiserverguide.api.member.domain.dto.MemberDto
import com.example.kotlinapiserverguide.common.http.domain.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.random.Random

@RestController
@RequestMapping("/api/test")
class TestController {
    @GetMapping("")
    fun test(): ApiResponse<MemberDto> {
        val intRange = (500..1000)
        val sleepTime = intRange.random().toLong()
        Thread.sleep(sleepTime)
        return ApiResponse(
            MemberDto(
                id = sleepTime,
                username = "test",
                name = "test",
                phoneNumber = "01011111111",
                createdAt = "2024/08/28 13:00:00",
                updatedAt = "2024/08/28 13:00:00",
                imageUrl = "http://localhost:8000",
            )
        )
    }
}