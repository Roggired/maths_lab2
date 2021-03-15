package view

import model.equation.createEquationFrom
import model.method.HalfDivisionMethod
import tornadofx.launch
import view.graph.GraphLauncher
import view.method.MethodPresenter
import java.io.PrintWriter
import kotlin.math.sign

class HalfDivisionMethodScene: Scene {
    override fun start(sceneContext: SceneContext) {
        println("Enter leftBound rightBound and accuracy:")

        var leftBound: Double
        var rightBound: Double
        var accuracy: Double
        while (true) {
            val userInput = readNotNullString()

            val parts = userInput.split(" ")

            try {
                leftBound = parts[0].toDouble()
                rightBound = parts[1].toDouble()
                accuracy = parts[2].toDouble()
                break
            } catch (e: Throwable) {
                println("Seems like you have written something wrong. Please, reenter:")
                continue
            }
        }

        Thread {
            launch<GraphLauncher>(
                "-2.7x^3-1.48x^2+19.23x+6.35",
                leftBound.toString(),
                rightBound.toString(),
                accuracy.toString()
            )
        }.start()

        println()
        println("I have drawn the graph for you! You can see it in the separate window.")
        println("Now let's find solutions for this equation.")

        val equation = createEquationFrom(sceneContext.equation)

        outerWhile@ while (true) {
            println("Please, enter left isolation border, right isolation border or STOP to stop finding solutions.")
            var leftIsolationBound: Double
            var rightIsolationBound: Double

            while (true) {
                val userInput = readNotNullString()

                if (userInput.toLowerCase() == "stop") {
                    println("Default equation has been defeated!")
                    println()
                    sceneContext.router.switch(sceneContext, "Welcome")
                    break@outerWhile
                }

                val parts = userInput.split(" ")

                try {
                    leftIsolationBound = parts[0].toDouble()
                    rightIsolationBound = parts[1].toDouble()

                    if (equation.evaluate(leftIsolationBound).sign * equation.evaluate(rightIsolationBound).sign > 0) {
                        println("No solutions on this isolation interval or function isn't monotonous")
                        continue
                    }

                    break
                } catch (e: Throwable) {
                    println("Seems like you have written something wrong. Please, reenter:")
                    continue
                }
            }

            val method = HalfDivisionMethod(equation, leftIsolationBound, rightIsolationBound, accuracy)
            val printWriter = PrintWriter(System.out)

            printWriter.println()
            printWriter.println("Half-Division method table")
            MethodPresenter.presentTable(method.getTable(), printWriter)

            printWriter.println()
            printWriter.println("Half-Division method solution on: $leftIsolationBound $rightIsolationBound")

            MethodPresenter.presentSolutions(method.getSolutions(), printWriter)
        }
    }

    private fun readNotNullString(): String {
        while (true) {
            val userInput = readLine()

            if (userInput != null) {
                return userInput
            }
            println("Seems like you have written nothing. Please, write:")
        }
    }
}