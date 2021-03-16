package model.method

import model.equation.Equation

abstract class Method(
    protected val equation: Equation,
    protected val leftBound: Double,
    protected val rightBound: Double,
    protected val accuracy: Double
) {
    abstract fun getTable(): ArrayList<Array<String>>

    abstract fun getSolutions(): ArrayList<Double>

    abstract fun getStepQuantity(): Int
}