package com.group.librayapp.calculator

import com.group.libraryapp.calculator.Calculator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*
import kotlin.IllegalArgumentException

internal class CalculatorTest {

    companion object {

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            println("beforeAll")
        }

        @JvmStatic
        @AfterAll
        fun afterAll() {
            println("beforeAll")
        }

    }

    @BeforeEach
    fun beforeEach() {
        println("beforeEach")
    }

    @AfterEach
    fun afterEach() {
        println("afterEach")
    }

    @Test
    fun add() {
        // given
        val calculator = Calculator(5)

        // when
        calculator.add(3)

        // then
        // assertEquals(8, calculator.number)
        assertThat(calculator.number).isEqualTo(8)
    }

    @Test
    fun minus() {
        // given
        val calculator = Calculator(5)

        // when
        calculator.minus(3)

        // then
        assertEquals(2, calculator.number)
    }

    @Test
    fun multiply() {
        // given
        val calculator = Calculator(5)

        // when
        calculator.multiply(3)

        // then
        assertEquals(15, calculator.number)
    }

    @Test
    fun divide_by_notzero() {
        // given
        val calculator = Calculator(5)

        // when
        calculator.divide(2)

        // then
        assertEquals(2, calculator.number)
    }

    @Test
    fun divide_by_zero() {
        // given
        val calculator = Calculator(5)

        // when

        // when & then
//        assertThrows(IllegalArgumentException::class.java) {
//            calculator.divide(0)
//        }

        assertThrows<IllegalArgumentException> {
            calculator.divide(0)
        }

    }
}