package com.group.libraryapp.calculator

import java.lang.IllegalArgumentException

fun main() {
    val calculatorTest = CalculatorTest()

    calculatorTest.addTest()
}

class CalculatorTest {

    fun addTest() {
        val calculator = Calculator(5)
        calculator.add(3)

        // Calculator 멤버 변수의 접근자가 private이기 때문에 이를 가져올 수도록 해야함.
        // 1. data class 활용
        val expectedCalculator = Calculator(8)

        if (calculator != expectedCalculator){
            throw IllegalStateException()
        }


    }

}