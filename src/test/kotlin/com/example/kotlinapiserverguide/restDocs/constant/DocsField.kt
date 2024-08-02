package com.example.kotlinapiserverguide.restDocs.constant

import org.springframework.restdocs.payload.FieldDescriptor


open class DocsField(path: String?) : FieldDescriptor(path) {

    var depth: Int = 1

    init {
        this.attributes(DocsAttributeKeys.DEPTH.depth(this.depth, this.path))
    }

    protected open var default: String
        get() = this.attributes.getOrDefault(DocsAttributeKeys.DEFAULT.name, "") as String
        set(value) {
            this.attributes(DocsAttributeKeys.DEFAULT.set(value))
        }

    protected open var format: String
        get() = this.attributes.getOrDefault(DocsAttributeKeys.FORMAT.name, "") as String
        set(value) {
            this.attributes(DocsAttributeKeys.FORMAT.set(value))
        }

    protected open var example: String
        get() = this.attributes.getOrDefault(DocsAttributeKeys.EXAMPLE.name, "") as String
        set(value) {
            this.attributes(DocsAttributeKeys.EXAMPLE.set(value))
        }

    open infix fun means(value: String): DocsField {
        this.description(value)
        return this
    }

    open infix fun attributes(block: DocsField.() -> Unit): DocsField {
        block()
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
        val fieldDepth = this.depth + 1
        fields.forEach {
            it.depth = fieldDepth
            val name = it.path.split(".").last()
            it.attributes(DocsAttributeKeys.DEPTH.depth(parentsDepth,"" ))
            it.attributes(DocsAttributeKeys.DEPTH.depth(fieldDepth, name))
        }
        return arrayOf(this, *fields)
    }

}
