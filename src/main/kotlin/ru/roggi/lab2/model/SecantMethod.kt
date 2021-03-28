package ru.roggi.lab2.model

import ru.roggi.comp.math.model.Equation
import kotlin.math.abs
import kotlin.math.sign

class SecantMethod(
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
        validateMethodCanBeApplied()

        val titles = arrayOf("Step №", "xk-1", "f(xk-1)", "xk", "f(xk)", "xk+1", "f(xk+1)", "|xk - xk+1|")
        table.add(titles)

        var xk_1 = calcX0()
        var xk = calcX1()
        var fxk_1 = equation.evaluate(xk_1)
        var fxk = equation.evaluate(xk)
        var dif = abs(xk - xk_1)
        var xk_next: Double = xk
        var fxk_next: Double = fxk

        while (dif >= accuracy && abs(fxk) >= accuracy) {
            xk_next = xk - fxk * ((xk - xk_1) / (fxk - fxk_1))
            fxk_next = equation.evaluate(xk_next)

            addToTable(step, xk_1, fxk_1, xk, fxk, xk_next, fxk_next, dif)
            addSolutions(xk_next, fxk_next, dif)
            step++

            xk_1 = xk
            fxk_1 = fxk
            xk = xk_next
            fxk = fxk_next
            dif = abs(xk - xk_1)
        }

        addToTable(step, xk_1, fxk_1, xk, fxk, xk_next, fxk_next, dif)
        addSolutions(xk_next, fxk_next, dif)
    }


    private fun validateMethodCanBeApplied() {
        val leftFValue = equation.evaluate(leftBound)
        val leftFSign = leftFValue.sign
        validationInfoBuilder.append(System.lineSeparator())
        validationInfoBuilder.append(presentFunctionValue(leftFSign, leftBound, leftFValue))
        validationInfoBuilder.append(System.lineSeparator())

        val leftFSDValue = equation.evaluateSecondDerivative(leftBound)
        val leftFSDSign = leftFSDValue.sign
        validationInfoBuilder.append(presentFunctionSecondDerivativeValue(leftFSDSign, leftBound, leftFSDValue))
        validationInfoBuilder.append(System.lineSeparator())
        validationInfoBuilder.append(System.lineSeparator())

        val leftSign = leftFSign * leftFSDSign
        validationInfoBuilder.append(presentFMultiplyFSD(leftSign, leftBound))
        validationInfoBuilder.append(System.lineSeparator())
        validationInfoBuilder.append(System.lineSeparator())

        val rightFValue = equation.evaluate(rightBound)
        val rightFSign = rightFValue.sign
        validationInfoBuilder.append(presentFunctionValue(rightFSign, rightBound, rightFValue))
        validationInfoBuilder.append(System.lineSeparator())

        val rightFSDValue = equation.evaluateSecondDerivative(rightBound)
        val rightFSDSign = rightFSDValue.sign
        validationInfoBuilder.append(presentFunctionSecondDerivativeValue(rightFSDSign, rightBound, rightFSDValue))
        validationInfoBuilder.append(System.lineSeparator())
        validationInfoBuilder.append(System.lineSeparator())

        val rightSign = rightFSign * rightFSDSign
        validationInfoBuilder.append(presentFMultiplyFSD(rightSign, rightBound))
        validationInfoBuilder.append(System.lineSeparator())
        validationInfoBuilder.append(System.lineSeparator())

        if (leftSign < 0 && rightSign < 0) {
            throw MethodCannotBeAppliedException()
        }
    }

    private fun presentFMultiplyFSD(sign: Double, x: Double): String {
        val format = getFormatBasedOnAccuracy(accuracy)
        return when(sign) {
            -1.0 -> "f($format) * f''($format) < 0".format(x, x)
            0.0 -> "f($format) * f''($format) = 0".format(x, x)
            else -> "f($format) * f''($format) > 0".format(x, x)
        }
    }

    private fun calcX0(): Double =
        if (equation.evaluate(rightBound).sign * equation.evaluateSecondDerivative(rightBound).sign >= 0)
            rightBound
        else
            leftBound

    private fun calcX1(): Double =
        if (equation.evaluate(rightBound).sign * equation.evaluateSecondDerivative(rightBound).sign >= 0)
            rightBound - 2 * accuracy
        else
            leftBound - 2 * accuracy

    private fun addToTable(step: Int, xk_1: Double, fxk_1: Double, xk: Double, fxk: Double, xk_next: Double, fxk_next: Double, dif: Double) =
        table.add(arrayOf(step.toString(), xk_1.toString(), fxk_1.toString(), xk.toString(), fxk.toString(), xk_next.toString(), fxk_next.toString(), dif.toString()))

    private fun addSolutions(xk_next: Double, fxk_next: Double, dif: Double) =
        (abs(fxk_next) <= accuracy || dif <= accuracy) && !solutions.contains(xk_next) && solutions.add(xk_next)

    override fun getTable(): ArrayList<Array<String>> = table

    override fun getSolutions(): ArrayList<Double> = solutions

    override fun getStepQuantity(): Int = step
}