package model.equation

import kotlin.math.abs
import kotlin.math.log
import kotlin.math.pow
import kotlin.math.tan

data class Equation(
    val tokens: Array<Term>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Equation

        if (!tokens.contentEquals(other.tokens)) return false

        return true
    }

    override fun hashCode(): Int {
        return tokens.contentHashCode()
    }

    override fun toString(): String {
        return "Equation(tokens=${tokens.contentToString()})"
    }

    fun evaluate(x: Double): Double {
        var result = 0.0
        tokens.forEach {
            result += it.evaluate(x)
        }
        return result
    }
}

class Sign(private val value: String) {
    fun applyTo(x: Double): Double {
        if (value == "+") {
            return x
        }

        if (value == "-") {
            return -x
        }

        TODO("value equals neither + nor -")
    }

    override fun toString(): String {
        return "Sign(value='$value')"
    }

    companion object {
        fun isSign(string: String): Boolean {
            return string == "+" || string == "-"
        }
    }
}

abstract class Term(
    protected val sign: Sign,
    protected val factor: Double
) {
    abstract fun evaluate(x: Double): Double
    override fun toString(): String {
        return "Term(sign=$sign, factor=$factor)"
    }
}

class LinearTerm(
    sign: Sign,
    factor: Double
): Term(sign, factor) {
    override fun evaluate(x: Double): Double {
        return sign.applyTo(factor * x)
    }
}

class ConstantTerm(
    sign: Sign,
    factor: Double
): Term(sign, factor) {
    override fun evaluate(x: Double): Double {
        return sign.applyTo(factor)
    }
}

abstract class ComplexTerm(
    sign: Sign,
    factor: Double,
    protected val baseTerm: Term
): Term(sign, factor) {
    override fun toString(): String {
        return "ComplexTerm(baseTerm=$baseTerm)" + " " + super.toString()
    }
}

class AbsTerm(
    sign: Sign,
    factor: Double,
    baseTerm: Term
): ComplexTerm(sign, factor, baseTerm) {
    override fun evaluate(x: Double): Double {
        return sign.applyTo(factor * abs(baseTerm.evaluate(x)))
    }
}

class PolynomialTerm(
    sign: Sign,
    factor: Double,
    baseTerm: Term,
    private val power: Double
) : ComplexTerm(sign, factor, baseTerm) {
    override fun evaluate(x: Double): Double {
        return sign.applyTo(factor * baseTerm.evaluate(x).pow(power))
    }

    override fun toString(): String {
        return "PolynomialTerm(power=$power)" + " " + super.toString()
    }
}

class TrigonometricTerm(
    sign: Sign,
    factor: Double,
    baseTerm: Term,
    private val function: (x: Double) -> Double
): ComplexTerm(sign, factor, baseTerm) {
    override fun evaluate(x: Double): Double {
        return sign.applyTo(factor * function(baseTerm.evaluate(x)))
    }

    override fun toString(): String {
        return "TrigonometricTerm(function=$function)" + " " + super.toString()
    }
}

fun ctg(x: Double): Double {
    return 1/ tan(x)
}

class LogarithmicTerm(
    sign: Sign,
    factor: Double,
    baseTerm: Term,
    private val logBase: Double
): ComplexTerm(sign, factor, baseTerm) {
    override fun evaluate(x: Double): Double {
        return sign.applyTo(factor * log(baseTerm.evaluate(x), logBase))
    }

    override fun toString(): String {
        return "LogarithmicTerm(logBase=$logBase)" + " " + super.toString()
    }
}