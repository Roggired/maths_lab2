package ru.roggi.lab2.view

import ru.roggi.lab2.model.HalfDivisionMethod
import ru.roggi.comp.math.GRAPH_ROUTE
import ru.roggi.comp.math.INPUT_BOUNDS_ROUTE
import ru.roggi.comp.math.model.Equation
import ru.roggi.comp.math.model.emptyEquation
import ru.roggi.comp.math.view.GraphIntent
import ru.roggi.comp.math.view.InputTwoIntent
import ru.roggi.comp.math.view.presenter.Presenter
import ru.roggi.console.application.model.State
import ru.roggi.console.application.view.scene.SceneContext
import ru.roggi.console.application.view.scene.StatefulScene
import ru.roggi.lab2.model.MethodException
import java.io.PrintWriter

class HalfDivisionMethodState: State {
    var equation: Equation = emptyEquation()
    var leftBound: Double = 0.0
    var rightBound: Double = 0.0
    var accuracy: Double = 0.0
}

class HalfDivisionMethodScene: StatefulScene<HalfDivisionMethodState>(HalfDivisionMethodState()) {
    override fun start(sceneContext: SceneContext) {
        sceneContext.router.switch(GRAPH_ROUTE) {
            it as GraphIntent
            state.equation = it.equation
            state.accuracy = it.accuracy
        }

        println("Now let's find solutions for this equation with half-division method.")

        var active = true
        while (active) {
            sceneContext.router.switch(INPUT_BOUNDS_ROUTE) {
                it as InputTwoIntent<*, *>
                state.leftBound = it.a as Double
                state.rightBound = it.b as Double
            }

            val method = HalfDivisionMethod(state.equation, state.leftBound, state.rightBound, state.accuracy)
            val printWriter = PrintWriter(System.out)

            printWriter.println()
            printWriter.println("Half-Division method table")
            try {
                printWriter.println(Presenter.present(method.getTable(), 15, 3))
            } catch (e: MethodException) {
                printWriter.println("On this isolation interval there are no or more than one solutions.")
                printWriter.println("Please, enter isolation interval again:")
                printWriter.flush()
                continue
            }

            printWriter.println()
            printWriter.println("Half-Division method solution on: ${state.leftBound} ${state.rightBound}")
            MethodPresenter.presentSolutions(method.getSolutions(), printWriter)

            printWriter.println()
            printWriter.println("Iterations quantity: ${method.getStepQuantity()}")
            printWriter.println()
            printWriter.flush()

            while (true) {
                println("Do you want to find one more solution with half-division method? (Y)es, (N)o:")
                val userInput = readLine() ?: continue

                if (userInput.toUpperCase() == "Y" || userInput.toLowerCase() == "yes") {
                    break
                }

                if (userInput.toUpperCase() == "N" || userInput.toLowerCase() == "no") {
                    active = false
                    break
                }
            }
        }
    }
}