package com.example.kotlinapiserverguide.restDocs.constant.parser

import com.example.kotlinapiserverguide.restDocs.constant.*
import com.example.kotlinapiserverguide.restDocs.infix.type
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import org.springframework.restdocs.snippet.Attributes.*

enum class ResponseFields(
    private var defaultSetting: ResponseFieldsSnippet
) {
    DEPTH_1(
        responseFields(
            attributes(
                DocsAttributeKeys.DEPTH.set(1)
            ),
            "resultCode" type STRING means "결과 코드",
            "resultMessage" type STRING means "결과 메세지",
        )
    ),
    DEPTH_2(
        responseFields(
            attributes(
                DocsAttributeKeys.DEPTH.set(1),
                DocsAttributeKeys.DEPTH.set(2)
            ),
            "resultCode" type STRING means "결과 코드",
            "resultMessage" type STRING means "결과 메세지",
        )
    ),
    DEPTH_3(
        responseFields(
            attributes(
                DocsAttributeKeys.DEPTH.set(1),
                DocsAttributeKeys.DEPTH.set(2),
                DocsAttributeKeys.DEPTH.set(3),
            ),
            "resultCode" type STRING means "결과 코드",
            "resultMessage" type STRING means "결과 메세지",
        )
    )
    ;

    fun build(vararg fields: DocsField): ResponseFieldsSnippet? {
        if (fields.isEmpty()) return this.defaultSetting.and("body" type NULL means "결과")

        return this.defaultSetting.and(
            *"body" type OBJECT means "결과" fields arrayOf(*fields)
        )
    }
}