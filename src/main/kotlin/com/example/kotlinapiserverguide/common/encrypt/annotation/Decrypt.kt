package com.example.kotlinapiserverguide.common.encrypt.annotation

import org.mapstruct.Qualifier

@Qualifier
@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
annotation class Decrypt()
