package com.example.kotlinapiserverguide.restDocs.constant

import org.springframework.restdocs.payload.FieldDescriptor

open class DocsField : FieldDescriptor {

    constructor(path: String?) : super(path) {
        this.attributes(DocsAttributeKeys.DEPTH.set(this.depth, this.path))
    }

    constructor(path: String, docsField: DocsField) : super(path) {
        this.type(docsField.type)
        this.description(docsField.description)
        this.depth = docsField.depth
        this.format = docsField.format
        this.default = docsField.default
        this.example = docsField.example
        if (docsField.isIgnored) this.ignored()
        if (docsField.isOptional) this.optional()
    }

    var depth: Int = 1

    protected open var default: String?
        get() = this.attributes.getOrDefault(DocsAttributeKeys.DEFAULT.code, "&nbsp;") as String
        set(value) {
            this.attributes(DocsAttributeKeys.DEFAULT.set(value))
        }

    protected open var format: String?
        get() = this.attributes.getOrDefault(DocsAttributeKeys.FORMAT.code, "&nbsp;") as String
        set(value) {
            this.attributes(DocsAttributeKeys.FORMAT.set(value))
        }

    protected open var example: String?
        get() = this.attributes.getOrDefault(DocsAttributeKeys.EXAMPLE.code, "&nbsp;") as String
        set(value) {
            this.attributes(DocsAttributeKeys.EXAMPLE.set(value))
        }

    open infix fun means(value: String): DocsField {
        this.description(value)
        return this
    }

    open infix fun defaultValue(value: String): DocsField {
        this.default = value
        return this
    }

    open infix fun formattedAs(value: String): DocsField {
        this.format = value
        return this
    }

    open infix fun example(value: String): DocsField {
        this.example = value
        return this
    }

    open infix fun isOptional(value: Boolean): DocsField {
        if (value) this.optional()
        return this
    }

    open infix fun isIgnored(value: Boolean): DocsField {
        if (value) this.ignored()
        return this
    }

    // is Last
    open infix fun fields(fields: Array<DocsField>): Array<DocsField> {
        val parentsDepth = this.depth
        val childFieldDepth = this.depth + 1
        val childFields: Array<DocsField> = fields
            .map { DocsField("${this.path}.${it.path}", it) }
            .toTypedArray()

        childFields.forEach {
            it.depth = childFieldDepth
            (1..parentsDepth).forEach { depth -> it.attributes(DocsAttributeKeys.DEPTH.set(depth, "&nbsp;")) }
            it.attributes(DocsAttributeKeys.DEPTH.set(childFieldDepth, it.path.split(".").last()))
        }

        return arrayOf(this, *childFields)
    }

}
