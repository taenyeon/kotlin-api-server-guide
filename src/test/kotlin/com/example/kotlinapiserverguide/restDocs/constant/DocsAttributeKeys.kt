package com.example.kotlinapiserverguide.restDocs.constant

import org.springframework.restdocs.snippet.Attributes.Attribute

enum class DocsAttributeKeys(
    private val code: String,
) {
    DEFAULT("defaultValue"),
    FORMAT("format"),
    EXAMPLE("example"),
    DEPTH("depth")
    ;

    fun set(value: String? = "-"): Attribute {
        return Attribute(this.code, value)
    }

    fun set(depth: Int): Attribute {
        return Attribute(this.code + depth, "-")
    }

    fun <T> set(depth: Int, value: T): Attribute {
        return Attribute(this.code + depth, value)
    }
}