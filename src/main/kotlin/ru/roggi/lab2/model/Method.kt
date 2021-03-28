package ru.roggi.lab2.model

import ru.roggi.comp.math.model.Equation
import kotlin.math.sign

abstract class Method(
    protected val equation: Equation,
    protected val leftBound: Double,
    protected val rightBound: Double,
    protected val accuracy: Double
) {
    protected val validationInfoBuilder = StringBuilder()
    protected val extraInfoBuilder = StringBuilder()


    fun getValidationInfo(): String = validationInfoBuilder.toString()

    fun getExtraInfo(): String? {
        val extraInfo = extraInfoBuilder.toString()
        return if (extraInfo.isEmpty()) {
            null
        } else {
            extraInfo
        }
    }

    abstract fun getTable(): ArrayList<Array<String>>

    abstract fun getSolutions(): ArrayList<Double>

    abstract fun getStepQuantity(): Int


    protected fun validateIsolationInterval() {
        val leftBoundFValue = equation.evaluate(leftBound)
        val leftBoundFSign = leftBoundFValue.sign
        validationInfoBuilder.append(presentFunctionValue(leftBoundFSign, leftBound, leftBoundFValue))
        validationInfoBuilder.append(System.lineSeparator())
        val rightBoundFValue = equation.evaluate(rightBound)
        val rightBoundFSign = rightBoundFValue.sign
        validationInfoBuilder.append(presentFunctionValue(rightBoundFSign, rightBound, rightBoundFValue))
        validationInfoBuilder.append(System.lineSeparator())

        if (leftBoundFSign * rightBoundFSign > 0) {
            throw InvalidIsolationIntervalException()
        }

        if (leftBoundFSign == 0.0 && rightBoundFSign == 0.0) {
            throw InvalidIsolationIntervalException()
        }
    }

    protected fun presentFunctionValue(sign: Double, x: Double, value: Double): String {
        val format = getFormatBasedOnAccuracy(accuracy)
        return when(sign) {
            -1.0 -> "f($format) = $format < 0".format(x, value)
            0.0 -> "f($format) = $format".format(x, value)
            else -> "f($format) = $format > 0".format(x, value)
        }
    }

    protected fun presentFunctionSecondDerivativeValue(sign: Double, x: Double, value: Double): String {
        val format = getFormatBasedOnAccuracy(accuracy)
        return when(sign) {
            -1.0 -> "f''($format) = $format < 0".format(x, value)
            0.0 -> "f''($format) = $format".format(x, value)
            else -> "f''($format) = $format > 0".format(x, value)
        }
    }
}