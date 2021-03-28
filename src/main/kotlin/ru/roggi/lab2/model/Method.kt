package ru.roggi.lab2.model

import ru.roggi.comp.math.model.Equation
import kotlin.math.sign

abstract class Method(
    protected val equation: Equation,
    protected val leftBound: Double,
    protected val rightBound: Double,
    protected val accuracy: Double
) {
    abstract fun getTable(): ArrayList<Array<String>>

    abstract fun getSolutions(): ArrayList<Double>

    abstract fun getStepQuantity(): Int


    protected fun validateIsolationInterval() {
        val leftBoundFSign = equation.evaluate(leftBound).sign
        val rightBoundFSign = equation.evaluate(rightBound).sign

        if (leftBoundFSign * rightBoundFSign > 0) {
            throw InvalidIsolationIntervalException()
        }

        if (leftBoundFSign == 0.0 && rightBoundFSign == 0.0) {
            throw InvalidIsolationIntervalException()
        }
    }
}