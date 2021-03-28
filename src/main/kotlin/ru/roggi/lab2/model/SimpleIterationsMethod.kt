package ru.roggi.lab2.model

import ru.roggi.comp.math.model.Equation
import ru.roggi.comp.math.model.LinearTerm
import ru.roggi.comp.math.model.Sign
import kotlin.math.abs
import kotlin.math.max

class SimpleIterationsMethod(
    equation: Equation,
    leftBound: Double,
    rightBound: Double,
    accuracy: Double
) : Method(
    equation,
    leftBound,
    rightBound,
    accuracy
) {
    private val table = ArrayList<Array<String>>()
    private val solutions = ArrayList<Double>()
    private val convergenceCondition = getConvergenceCondition()
    private var step = 1
    private val phiEquation: Equation
    private val q: Double


    init {
        phiEquation = findPhiEquation()
        q = findQ()

        validateIsolationInterval()
        validateMethodCanBeApplied()

        val titles = arrayOf("Step №", "xk", "f(xk)", "xk+1", "phi(xk)", "|xk - xk+1|")
        table.add(titles)

        var xk = leftBound
        var fxk = equation.evaluate(xk)
        var xk_next = phiEquation.evaluate(xk)
        var dif = abs(xk_next - xk)
        addToTable(step, xk, fxk, xk_next, dif)
        addToSolutions(xk, fxk, dif)

        while (convergenceCondition(xk, xk_next)) {
            step++

            xk = xk_next
            fxk = equation.evaluate(xk)
            xk_next = phiEquation.evaluate(xk)
            dif = abs(xk_next - xk)
            addToTable(step, xk, fxk, xk_next, dif)
            addToSolutions(xk, fxk, dif)
        }
    }


    private fun findPhiEquation(): Equation {
        val lambda = -1.0 / max(equation.evaluateFirstDerivative(leftBound), equation.evaluateFirstDerivative(rightBound))
        val linearTerm = LinearTerm(Sign("+"), 1.0)

        val phiEquation = equation.clone()
        phiEquation.multiplyOnConstant(lambda)
        phiEquation.addTerm(linearTerm)

        return phiEquation
    }

    private fun findQ(): Double = max(abs(phiEquation.evaluateFirstDerivative(leftBound)), abs(phiEquation.evaluateFirstDerivative(rightBound)))

    private fun validateMethodCanBeApplied() {
        if (abs(phiEquation.evaluateFirstDerivative(leftBound)) >= 1 || abs(phiEquation.evaluateFirstDerivative(rightBound)) >= 1) {
            throw MethodCannotBeAppliedException()
        }
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
        dif: Double
    ) =
        table.add(arrayOf(step.toString(), xk.toString(), fxk.toString(), xk_next.toString(), xk_next.toString(), dif.toString()))

    private fun addToSolutions(xk: Double, fxk: Double, dif: Double) =
        (abs(fxk) <= accuracy || dif <= accuracy) && !solutions.contains(xk) && solutions.add(xk)

    override fun getTable(): ArrayList<Array<String>> = table

    override fun getSolutions(): ArrayList<Double> = solutions

    override fun getStepQuantity(): Int = step
}