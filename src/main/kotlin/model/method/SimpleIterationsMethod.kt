package model.method

import model.equation.Equation
import kotlin.math.abs

class SimpleIterationsMethod(
    private val q: Double,
    private val phiEquation: Equation,
    private val x0: Double,
    equation: Equation,
    leftBound: Double,
    rightBound: Double,
    accuracy: Double
): Method(equation, leftBound, rightBound, accuracy) {
    private val solutions = ArrayList<Double>()
    private val convergenceCondition = getConvergenceCondition()
    private var step = 1


    override fun getTable(): ArrayList<Array<String>> {
        val table = ArrayList<Array<String>>()
        val titles = arrayOf("Step №", "xk", "f(xk)", "xk+1", "phi(xk)", "|xk - xk+1|")
        table.add(titles)

        var xk = x0
        var fxk = equation.evaluate(xk)
        var xk_next = phiEquation.evaluate(xk)
        var dif = abs(xk_next - xk)
        addToTable(step, xk, fxk, xk_next, dif, table)
        addToSolutions(xk, fxk, dif)

        while (convergenceCondition(xk, xk_next)) {
            step++

            xk = xk_next
            fxk = equation.evaluate(xk)
            xk_next = phiEquation.evaluate(xk)
            dif = abs(xk_next - xk)
            addToTable(step, xk, fxk, xk_next, dif, table)
            addToSolutions(xk, fxk, dif)
        }

        return table
    }

    private fun getConvergenceCondition(): (xk: Double, xk_next: Double) -> Boolean =
        if (q > 0.5)
            fun(xk: Double, xk_next: Double) = abs(xk - xk_next) > accuracy
        else
            fun(xk: Double, xk_next: Double) = abs(xk - xk_next) >= accuracy * (1 - q) / q

    private fun addToTable(
        step: Int,
        xk: Double,
        fxk: Double,
        xk_next: Double,
        dif: Double,
        table: ArrayList<Array<String>>
    ) =
        table.add(arrayOf(step.toString(), xk.toString(), fxk.toString(), xk_next.toString(), xk_next.toString(), dif.toString()))

    private fun addToSolutions(xk: Double, fxk: Double, dif: Double) =
        (abs(fxk) <= accuracy || dif <= accuracy) && !solutions.contains(xk) && solutions.add(xk)

    override fun getSolutions(): ArrayList<Double> = solutions

    override fun getStepQuantity(): Int = step
}