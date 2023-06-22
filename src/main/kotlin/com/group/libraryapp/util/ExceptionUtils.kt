package com.group.libraryapp.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.findByIdOrNull
import java.lang.IllegalArgumentException

fun fail(): Nothing {
    throw IllegalArgumentException()
}

fun <T, ID> CrudRepository<T, ID>.findByIdOrThrow(id: ID): T {
    return this.findByIdOrNull(id) ?: fail()
}

inline fun <reified T> logger(): Logger = LoggerFactory.getLogger(T::class.java)