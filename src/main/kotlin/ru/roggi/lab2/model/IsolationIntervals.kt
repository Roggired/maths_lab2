package ru.roggi.lab2.model

import ru.roggi.comp.math.model.Equation
import kotlin.math.sign

class IsolationIntervals(private val intervals: Collection<Pair<Double, Double>>) {
    private var iterator = intervals.iterator()


    fun restart() {
        iterator = intervals.iterator()
    }

    fun hasNext(): Boolean = iterator.hasNext()

    fun next(): Pair<Double, Double> = iterator.next()
}


class IsolationIntervalsTable(equation: Equation) {
    val isolationIntervals: IsolationIntervals
    val table: ArrayList<Array<String>>

    init {
        val intervals = mutableListOf<Pair<Double, Double>>()
        table = ArrayList()
        table.add(arrayOf("x", "f(x)"))

        var prevX = -50.0
        var prevFx = equation.evaluate(prevX)
        var x = -49.9
        var fx = equation.evaluate(x)
        while (x <= 50.0) {
            table.add(arrayOf(x.toString(), fx.toString()))

            if (prevFx.sign * fx.sign <= 0) {
                intervals.add(prevX to x)
            }

            prevX = x
            prevFx = fx
            x += 0.1
            fx = equation.evaluate(x)
        }

        isolationIntervals = IsolationIntervals(intervals)
    }
}