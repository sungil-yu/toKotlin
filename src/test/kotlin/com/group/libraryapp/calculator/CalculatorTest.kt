package com.group.libraryapp.calculator


fun main() {
    val calculatorTest = CalculatorTest()

    calculatorTest.addTest()
}

class CalculatorTest {

    fun addTest() {
        //given
        val calculator = Calculator(5)
        //when
        calculator.add(3)

        // Calculator 멤버 변수의 접근자가 private 이를 가져올 수도록 해야함.
        // 1. data class 활용
        //        val expectedCalculator = Calculator(8)
        //
        //        if (calculator != expectedCalculator){
        //            throw IllegalStateException()
        //        }

        //2. number 변수에 접근하여 값을 가져오기. 접근 지시자 public 변경
        //        if (calculator.number != 8) {
        //            throw IllegalStateException()
        //        }


        //3. Backing Property 사용하기. ( the method I chose )
        //then
        if (calculator.number != 8) {
            throw IllegalStateException()
        }
    }


}