package com.group.libraryapp

import com.group.libraryapp.calculator.Calculator
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException

class JunitCalculatorTest {


    @Test
    fun add() {
        val calculator = Calculator(5)

        calculator.add(3)

        assertThat(calculator.number).isEqualTo(8)
    }

    @Test
    fun minus() {
        val calculator = Calculator(5)

        calculator.minus(3)

        assertThat(calculator.number).isEqualTo(2)
    }

    @Test
    fun multiply() {
        val calculator = Calculator(5)

        calculator.multiply(3)

        assertThat(calculator.number).isEqualTo(15)
    }

    @Test
    fun divide() {
        val calculator = Calculator(5)

        calculator.divide(2)

        assertThat(calculator.number).isEqualTo(2)
    }

    @Test
    fun divideException() {
        val calculator = Calculator(5)


        val message = assertThrows<IllegalArgumentException> {
            calculator.divide(0)
        }.message

        assertThat(message).isEqualTo("0으로 나눌 수 없습니다.")
    }

}