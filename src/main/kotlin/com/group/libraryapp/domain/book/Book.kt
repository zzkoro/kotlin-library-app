package com.group.libraryapp.domain.book

import java.lang.IllegalArgumentException
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Book(
    val name: String,
    val type: BookType,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

) {
    init {
        if (name.isBlank()) {
            throw IllegalArgumentException("이름은 비어 있을 수 없습니다.")
        }
    }

    companion object {
        fun fixture(
            name: String = "책이름",
            type: BookType = BookType.COMPUTER,
            id: Long? = null
        ): Book {
            return Book(name = name, type = type, id = id)

        }
    }
}