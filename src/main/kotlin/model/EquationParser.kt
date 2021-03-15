package model

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

fun createEquationFrom(string: String): Equation {
    val terms: ArrayList<Term> = ArrayList()

    var inTerm = false
    var openParenthesises = 0
    var termSign = "+"
    var termFactor = ""
    var term = ""

    string.forEach {
        if (!inTerm) {
            when (it) {
                '+' -> {
                    termSign = "+"
                    return@forEach
                }
                '-' -> {
                    termSign = "-"
                    return@forEach
                }
                '.' -> {
                    termFactor += "."
                    return@forEach
                }
                ',' -> {
                    termFactor += ','
                    return@forEach
                }
            }

            if (it.isDigit()) {
                termFactor += it
            } else {
                inTerm = true
            }
        }

        if (inTerm) {
            when (it) {
                '(' -> openParenthesises++
                ')' -> openParenthesises--
                '+', '-' -> {
                    if (openParenthesises == 0) {
                        inTerm = false
                        parseTerms(termSign, termFactor, term, terms)
                        termSign = it.toString()
                        termFactor = ""
                        term = ""
                        return@forEach
                    }
                }
            }

            term += it
        }
    }

    parseTerms(termSign, termFactor, term, terms)

    return Equation(terms.toTypedArray())
}

private fun createLinearTerm(sign: String, factor: String): Term {
    return LinearTerm(Sign(sign), factor.toDouble())
}

private fun parseTerms(sign: String, factor: String, term: String, terms: ArrayList<Term>) {
    if (term.isEmpty()) {
        terms.add(ConstantTerm(Sign(sign), factor.toDouble()))
    }

    val termsStack: ArrayDeque<String> = ArrayDeque()

    var currentTerm = term
    var left: Int
    var right: Int

    while (currentTerm.isNotEmpty()) {
        left = 0
        right = currentTerm.length - 1

        while (left < currentTerm.length - 1 && currentTerm[left] != '(') {
            left++
        }

        if (left == currentTerm.length - 1) {
            termsStack.addLast(currentTerm)
            break
        }

        while (right > 0 && currentTerm[right] != ')') {
            right--
        }

        if (right == 0) {
            termsStack.addLast(currentTerm)
            break
        }

        termsStack.addLast(currentTerm.substring(0, left))
        currentTerm = currentTerm.substring(left + 1, right)
    }

    var tempTerm: Term = createLinearTerm("+", "1")

    for (i in termsStack.size - 1 downTo 0) {
        if (i == 0) {
            var factor = factor

            if (factor.isEmpty()) {
                factor = "1"
            }

            terms.add(createTerm(sign, factor, termsStack[i], tempTerm))
        } else {
            val sign = getSign(termsStack[i])
            var factor = getFactor(termsStack[i])

            var targetIndex = factor.length

            if (sign == "-") {
                targetIndex++
            }

            if (factor.isEmpty()) {
                factor = "1"
            }

            tempTerm = createTerm(sign, factor, termsStack[i].substring(targetIndex), tempTerm)
        }
    }
}

private fun getSign(term: String): String {
    if (term.startsWith("-")) {
        return "-"
    }

    return "+"
}

private fun getFactor(term: String): String {
    var factor = ""

    term.forEach {
        if (it != '+' && it != '-') {
            if (it.isDigit()) {
                factor += it
            } else {
                return factor
            }
        }
    }

    return factor
}

private fun createTerm(signStr: String, factorStr: String, term: String, baseTerm: Term): Term {
    val sign = Sign(signStr)
    val factor = factorStr.toDouble()

    if (term.isEmpty()) {
        return ConstantTerm(sign, factor)
    }

    when (term) {
        "sin" -> return TrigonometricTerm(sign, factor, baseTerm, ::sin)
        "cos" -> return TrigonometricTerm(sign, factor, baseTerm, ::cos)
        "tg" -> return TrigonometricTerm(sign, factor, baseTerm, ::tan)
        "ctg" -> return TrigonometricTerm(sign, factor, baseTerm, ::ctg)
    }

    if (term.contains("log")) {
        return LogarithmicTerm(sign, factor, baseTerm, term.substring(term.indexOf("^") + 1).toDouble())
    }

    if (term.contains("^") && term.contains("x")) {
        return PolynomialTerm(sign, factor, createLinearTerm("+", "1"), term.substring(term.indexOf("^") + 1).toDouble())
    }

    if (term.contains("^")) {
        return PolynomialTerm(sign, factor, baseTerm, term.substring(term.indexOf("^") + 1).toDouble())
    }

    return LinearTerm(sign, factor)
}