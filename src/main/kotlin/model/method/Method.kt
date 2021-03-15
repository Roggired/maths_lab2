package model.method

import model.equation.Equation

abstract class Method(protected val equation: Equation) {
    abstract fun getTable(): ArrayList<Array<String>>

    abstract fun getSolutions(): ArrayList<Double>
}