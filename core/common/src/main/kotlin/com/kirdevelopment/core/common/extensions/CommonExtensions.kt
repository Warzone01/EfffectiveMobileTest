package com.kirdevelopment.core.common.extensions

fun String?.orEmptyIfNull(): String = this ?: ""

fun String?.isNotNullOrBlank(): Boolean = !this.isNullOrBlank()
