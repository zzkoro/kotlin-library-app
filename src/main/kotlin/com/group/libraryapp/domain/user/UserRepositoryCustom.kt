package com.group.libraryapp.domain.user

interface UserRepositoryCustom {

    fun findAllWithHistoriesByDSL(): List<User>
}