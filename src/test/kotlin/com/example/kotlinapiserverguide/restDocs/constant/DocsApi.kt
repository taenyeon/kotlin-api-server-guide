package com.example.kotlinapiserverguide.restDocs.constant

enum class DocsApi(
    var docsApiType: DocsApiType,
    var directoryName: String,
    var description: String,
) {

    // USER
    USER_JOIN(DocsApiType.USER, "/user-join", "유저 회원가입"),
    USER_LOGIN(DocsApiType.USER, "/user-login", "유저 로그인"),
    USER_LOGOUT(DocsApiType.USER, "/user-logout", "유저 로그아웃"),

    // MEMBER
    MEMBER_FIND(DocsApiType.MEMBER, "/user-find", "회원 조회"),

    ;
}