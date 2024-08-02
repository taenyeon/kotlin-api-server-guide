package com.example.kotlinapiserverguide.restDocs.infix

import com.example.kotlinapiserverguide.restDocs.constant.DocsField
import com.example.kotlinapiserverguide.restDocs.constant.DocsFieldType
import org.springframework.restdocs.payload.JsonFieldType

private fun createField(value: String, type: JsonFieldType, optional: Boolean = false): DocsField {
    val docsField = DocsField(value)
    docsField.type(type)
    docsField.description("")
    if (optional) docsField.optional()
    return docsField
}

infix fun String.type(docsFieldType: DocsFieldType): DocsField {
    return createField(this, docsFieldType.type)
}