package ru.roggi.lab2.view

import ru.roggi.comp.math.GRAPH_ROUTE
import ru.roggi.comp.math.YES_NO_ROUTE
import ru.roggi.comp.math.model.Equation
import ru.roggi.comp.math.model.createEquationFrom
import ru.roggi.comp.math.model.emptyEquation
import ru.roggi.comp.math.view.GraphIntent
import ru.roggi.comp.math.view.InputTwoIntent
import ru.roggi.comp.math.view.YesNoIntent
import ru.roggi.comp.math.view.presenter.Presenter
import ru.roggi.console.application.model.State
import ru.roggi.console.application.view.scene.SceneContext
import ru.roggi.console.application.view.scene.StatefulScene
import ru.roggi.lab2.INPUT_METHOD_BOUNDS_ROUTE
import ru.roggi.lab2.model.InvalidIsolationIntervalException
import ru.roggi.lab2.model.Method
import ru.roggi.lab2.model.MethodCannotBeAppliedException

const val DEFAULT_EQUATION = "-2,7x^3 - 1,48x^2 + 19,23x + 6,35"
const val SYMBOLS_PER_TABLE_COLUMN = 10
const val SYMBOLS_AFTER_DOT = 4

class MethodState: State {
    var equation: Equation = emptyEquation()
    var leftBound: Double = 0.0
    var rightBound: Double = 0.0
    var accuracy: Double = 0.0
}

class MethodScene(
    private val methodName: String,
    private val methodBuilder: (MethodState) -> Method
): StatefulScene<MethodState>(MethodState()) {
    override fun start(sceneContext: SceneContext) {
        var useDefaultEquation = false
        sceneContext.put("greetings", "Do you want to use the default equation: $DEFAULT_EQUATION?")
        sceneContext.router.switch(YES_NO_ROUTE) {
            it as YesNoIntent
            useDefaultEquation = it.isYes
        }

        println("Firstly, let's build a graph.")
        if (useDefaultEquation) {
            sceneContext.put("equation", createEquationFrom(DEFAULT_EQUATION))
        }
        sceneContext.router.switch(GRAPH_ROUTE) {
            it as GraphIntent
            state.equation = it.equation
            state.accuracy = it.accuracy
        }


        println("Now let's find solutions for the equation with $methodName method.")
        println("Chosen equation: ${Presenter.present(state.equation)}")
        println("Chosen accuracy: ${"%.${SYMBOLS_AFTER_DOT}f".format(state.accuracy)}")
        println()

        var active = true
        while (active) {
            sceneContext.router.switch(INPUT_METHOD_BOUNDS_ROUTE) {
                it as InputTwoIntent<*, *>
                state.leftBound = it.a as Double
                state.rightBound = it.b as Double
            }

            var method: Method
            try {
                method = methodBuilder(state)
            } catch (e: InvalidIsolationIntervalException) {
                println("On this isolation interval there are no or more than one solutions.")
                println("Please, enter isolation interval again:")
                continue
            } catch (e: MethodCannotBeAppliedException) {
                println("On this isolation interval $methodName method cannot be applied.")
                println("Please, enter isolation interval again:")
                continue
            }

            println("$methodName method table")
            println(Presenter.present(method.getTable(), SYMBOLS_PER_TABLE_COLUMN, SYMBOLS_AFTER_DOT))
            println()

            println("$methodName method solution on: ${state.leftBound} ${state.rightBound}")
            println(presentSolutions(method.getSolutions()))
            println()

            println("Iterations quantity: ${method.getStepQuantity()}")
            println()

            sceneContext.put("greetings", "Do you want to find one more solution with $methodName method?")
            sceneContext.router.switch(YES_NO_ROUTE) {
                it as YesNoIntent
                active = it.isYes
            }
        }
        sceneContext.router.switch("main")
    }

    private fun presentSolutions(solutions: ArrayList<Double>): String {
        val stringBuilder = StringBuilder()
        solutions.forEach {
            stringBuilder.append("x=%.${SYMBOLS_AFTER_DOT}f, f(x)=%.${SYMBOLS_AFTER_DOT}f".format(it, state.equation.evaluate(it)))
            stringBuilder.append(" ")
        }
        return stringBuilder.toString()
    }
}