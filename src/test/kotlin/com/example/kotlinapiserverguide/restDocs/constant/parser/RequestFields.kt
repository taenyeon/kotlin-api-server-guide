package com.example.kotlinapiserverguide.restDocs.constant.parser

import com.example.kotlinapiserverguide.restDocs.constant.DocsAttributeKeys
import com.example.kotlinapiserverguide.restDocs.constant.DocsField
import com.example.kotlinapiserverguide.restDocs.constant.OBJECT
import com.example.kotlinapiserverguide.restDocs.constant.STRING
import com.example.kotlinapiserverguide.restDocs.infix.type
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import org.springframework.restdocs.snippet.Attributes.*

enum class RequestFields(
    private var defaultSetting: RequestFieldsSnippet
) {
    DEPTH_1(
        requestFields(
            attributes(
                DocsAttributeKeys.DEPTH.set(1)
            ),
        )
    ),
    DEPTH_2(
        requestFields(
            attributes(
                DocsAttributeKeys.DEPTH.set(1),
                DocsAttributeKeys.DEPTH.set(2)
            ),
        )
    ),
    DEPTH_3(
        requestFields(
            attributes(
                DocsAttributeKeys.DEPTH.set(1),
                DocsAttributeKeys.DEPTH.set(2),
                DocsAttributeKeys.DEPTH.set(3),
            ),
        )
    )
    ;

    fun build(vararg fields: FieldDescriptor): RequestFieldsSnippet? {
        return this.defaultSetting.and(
            *fields
        )
    }
}