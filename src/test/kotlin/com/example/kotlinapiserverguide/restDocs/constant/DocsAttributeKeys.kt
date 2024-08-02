package com.example.kotlinapiserverguide.restDocs.constant

import org.springframework.restdocs.snippet.Attributes.Attribute

enum class DocsAttributeKeys(
    val code: String,
) {
    DEFAULT("defaultValue"),
    FORMAT("format"),
    EXAMPLE("example"),
    DEPTH("depth")
    ;

    fun <T> set(value: T): Attribute {
        return Attribute(this.code, value)
    }

    fun <T> depth(depth: Int, value: T): Attribute {
        return Attribute(this.code + depth, value)
    }
}