package com.example.kotlinapiserverguide.api.member.repository

import com.example.kotlinapiserverguide.api.member.domain.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member, Long> {

    fun findByUsername(username: String?): Member?

}