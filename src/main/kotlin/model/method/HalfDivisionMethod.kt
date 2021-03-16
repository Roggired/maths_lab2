package model.method

import model.equation.Equation
import kotlin.math.abs
import kotlin.math.sign

class HalfDivisionMethod(
    equation: Equation,
    leftBound: Double,
    rightBound: Double,
    accuracy: Double
): Method(equation, leftBound, rightBound, accuracy) {
    private val solutions: ArrayList<Double> = ArrayList()


    override fun getTable(): ArrayList<Array<String>> {
        val table: ArrayList<Array<String>> = ArrayList()
        val titles = arrayOf("Step №", "a", "b", "x", "f(a)", "f(b)", "f(x)", "|b-a|")
        table.add(titles)
        calcInRange(leftBound, rightBound, table, 1)
        return table
    }

    private fun calcInRange(a: Double, b: Double, table: ArrayList<Array<String>>, step: Int): Int {
        var a = a
        var b = b
        var x = (a + b) / 2
        var fx = equation.evaluate(x)
        var fa = equation.evaluate(a)
        var fb = equation.evaluate(b)
        var dif = abs(b - a)
        var signOnAB = fa.sign * fb.sign
        addToTable(step, a, fa, b, fb, x, fx, dif, table)
        addSolutions(a, fa, b, fb, x, fx, dif)
        var step = step

        while (dif > accuracy && abs(fx) > accuracy && signOnAB <= 0) {
            step++
            val leftSign = fa.sign * fx.sign
            val rightSign = fx.sign * fb.sign

            if (leftSign <= 0 && rightSign <= 0) {
                step = calcInRange(a, x, table, step)
                step = calcInRange(x, b, table, step)
                return step
            }

            if (leftSign <= 0) {
                signOnAB = leftSign
                b = x
                fb = fx
            }

            if (rightSign <= 0) {
                signOnAB = rightSign
                a = x
                fa = fx
            }

            x = (a + b) / 2
            fx = equation.evaluate(x)
            dif = abs(b - a)
            addToTable(step, a, fa, b, fb, x, fx, dif, table)
            addSolutions(a, fa, b, fb, x, fx, dif)
        }

        return step + 1
    }

    private fun addToTable(step: Int, a: Double, fa: Double, b: Double, fb: Double, x: Double, fx: Double, dif: Double, table: ArrayList<Array<String>>) {
        table.add(arrayOf(step.toString(), a.toString(), b.toString(), x.toString(), fa.toString(), fb.toString(), fx.toString(), dif.toString()))
    }

    private fun addSolutions(a: Double, fa: Double, b: Double, fb: Double, x: Double, fx: Double, dif: Double) {
        if (abs(fa) <= accuracy && !solutions.contains(a)) solutions.add(a)
        if (abs(fb) <= accuracy && !solutions.contains(b)) solutions.add(b)
        if ((abs(fx) <= accuracy || dif <= accuracy) && !solutions.contains(x)) solutions.add(x)
    }

    override fun getSolutions(): ArrayList<Double> {
        return solutions
    }
}