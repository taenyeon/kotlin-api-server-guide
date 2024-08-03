package com.example.kotlinapiserverguide.restDocs.constant

enum class DocsApi(
    var docsApiType: DocsApiType,
    var directoryName: String,
    var title: String,
    var description: String,
) {

    // USER
    USER_JOIN(DocsApiType.USER, "user-join", "유저 회원가입", "유저 회원가입을 한다."),
    USER_LOGIN(DocsApiType.USER, "user-login", "유저 로그인", "유저 로그인을 시도한다."),
    USER_LOGOUT(DocsApiType.USER, "user-logout", "유저 로그아웃", "유저 로그아웃을 한다."),

    // MEMBER
    MEMBER_FIND(DocsApiType.MEMBER, "user-find", "회원 조회(로그인 ID)", "로그인 ID로 회원을 조회한다."),

    ;
}