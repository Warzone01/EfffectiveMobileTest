package com.kirdevelopment.core.common.mapper

interface Mapper<in I, out O> {
    fun map(input: I): O
}

interface BiMapper<in I1, in I2, out O> {
    fun map(input1: I1, input2: I2): O
}
