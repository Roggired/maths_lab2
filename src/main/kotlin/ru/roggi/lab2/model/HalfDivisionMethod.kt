package ru.roggi.lab2.model

import ru.roggi.comp.math.model.Equation
import kotlin.math.abs
import kotlin.math.sign

class HalfDivisionMethod(
    equation: Equation,
    leftBound: Double,
    rightBound: Double,
    accuracy: Double
): Method(equation, leftBound, rightBound, accuracy) {
    private val table: ArrayList<Array<String>> = ArrayList()
    private val solutions: ArrayList<Double> = ArrayList()
    private var step = 1


    init {
        validateIsolationInterval()

        val titles = arrayOf("Step №", "a", "b", "x", "f(a)", "f(b)", "f(x)", "|b-a|")
        table.add(titles)
        calcInRange(leftBound, rightBound)
    }


    private fun calcInRange(a: Double, b: Double): Int {
        var a = a
        var b = b
        var x = (a + b) / 2
        var fx = equation.evaluate(x)
        var fa = equation.evaluate(a)
        var fb = equation.evaluate(b)
        var dif = abs(b - a)
        addToTable(step, a, fa, b, fb, x, fx, dif)

        if (abs(fa) < accuracy) {
            solutions.add(a)
            return step
        }

        if (abs(fb) < accuracy) {
            solutions.add(b)
            return step
        }

        addSolutions(a, fa, b, fb, x, fx, dif)

        while (dif >= accuracy && abs(fx) >= accuracy) {
            step++
            val leftSign = fa.sign * fx.sign
            val rightSign = fx.sign * fb.sign

            if (leftSign <= 0) {
                b = x
                fb = fx
            }

            if (rightSign <= 0) {
                a = x
                fa = fx
            }

            x = (a + b) / 2
            fx = equation.evaluate(x)
            dif = abs(b - a)
            addToTable(step, a, fa, b, fb, x, fx, dif)
            addSolutions(a, fa, b, fb, x, fx, dif)
        }

        return step + 1
    }

    private fun addToTable(step: Int, a: Double, fa: Double, b: Double, fb: Double, x: Double, fx: Double, dif: Double) {
        table.add(arrayOf(step.toString(), a.toString(), b.toString(), x.toString(), fa.toString(), fb.toString(), fx.toString(), dif.toString()))
    }

    private fun addSolutions(a: Double, fa: Double, b: Double, fb: Double, x: Double, fx: Double, dif: Double) {
        if (abs(fa) <= accuracy && !solutions.contains(a)) solutions.add(a)
        if (abs(fb) <= accuracy && !solutions.contains(b)) solutions.add(b)
        if ((abs(fx) <= accuracy || dif <= accuracy) && !solutions.contains(x)) solutions.add(x)
    }

    override fun getTable(): ArrayList<Array<String>> = table

    override fun getSolutions(): ArrayList<Double> = solutions

    override fun getStepQuantity(): Int = step
}