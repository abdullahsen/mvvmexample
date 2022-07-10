package com.asen.mvvmexample.util

interface Mapper<I, O> {
    fun map(input: I): O
}