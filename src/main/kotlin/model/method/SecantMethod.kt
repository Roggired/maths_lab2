package model.method

import model.equation.Equation
import kotlin.math.abs
import kotlin.math.sign

class SecantMethod(
    equation: Equation,
    private val secondDerivativeInLeftBound: Double,
    private val secondDerivativeInRightBound: Double,
    leftBound: Double,
    rightBound: Double,
    accuracy: Double
): Method(equation, leftBound, rightBound, accuracy) {
    private val solutions: ArrayList<Double> = ArrayList()


    override fun getTable(): ArrayList<Array<String>> {
        val table = ArrayList<Array<String>>()
        val titles = arrayOf("Step №", "xk-1", "f(xk-1)", "xk", "f(xk)", "xk+1", "f(xk+1)", "|xk - xk+1|")
        table.add(titles)

        var xk_1 = calcX0()
        var xk = calcX1()
        var fxk_1 = equation.evaluate(xk_1)
        var fxk = equation.evaluate(xk)
        var dif = abs(xk - xk_1)
        var step = 1
        var xk_next: Double = xk
        var fxk_next: Double = fxk

        while (dif > accuracy && abs(fxk) > accuracy) {
            xk_next = xk - fxk * ((xk - xk_1) / (fxk - fxk_1))
            fxk_next = equation.evaluate(xk_next)

            addToTable(step, xk_1, fxk_1, xk, fxk, xk_next, fxk_next, dif, table)
            addSolutions(xk_next, fxk_next, dif)
            step++

            xk_1 = xk
            fxk_1 = fxk
            xk = xk_next
            fxk = fxk_next
            dif = abs(xk - xk_1)
        }

        addToTable(step, xk_1, fxk_1, xk, fxk, xk_next, fxk_next, dif, table)
        addSolutions(xk_next, fxk_next, dif)

        return table
    }

    private fun calcX0(): Double =
        if (equation.evaluate(rightBound).sign * secondDerivativeInRightBound.sign >= 0)
            rightBound
        else
            leftBound

    private fun calcX1(): Double =
        if (equation.evaluate(rightBound).sign * secondDerivativeInRightBound.sign >= 0)
            rightBound - 2 * accuracy
        else
            leftBound - 2 * accuracy

    private fun addToTable(step: Int, xk_1: Double, fxk_1: Double, xk: Double, fxk: Double, xk_next: Double, fxk_next: Double, dif: Double, table: ArrayList<Array<String>>) =
        table.add(arrayOf(step.toString(), xk_1.toString(), fxk_1.toString(), xk.toString(), fxk.toString(), xk_next.toString(), fxk_next.toString(), dif.toString()))

    private fun addSolutions(xk_next: Double, fxk_next: Double, dif: Double) =
        (abs(fxk_next) <= accuracy || dif <= accuracy) && !solutions.contains(xk_next) && solutions.add(xk_next)

    override fun getSolutions(): ArrayList<Double> = solutions
}