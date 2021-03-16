package view.method

import model.equation.Equation
import view.Scene
import view.SceneContext
import kotlin.math.sign

abstract class MethodScene: Scene {
    fun readIsolationBorders(sceneContext: SceneContext, equation: Equation): Pair<Double, Double>? {
        println("Please, enter left isolation border, right isolation border or STOP to stop finding solutions.")
        var leftIsolationBound: Double
        var rightIsolationBound: Double

        while (true) {
            val userInput = readNotNullString()

            if (userInput.toLowerCase() == "stop") {
                println("Default equation has been defeated!")
                println()
                sceneContext.graphThread?.interrupt()
                sceneContext.router.switch(sceneContext, "Welcome")
                return null
            }

            val parts = userInput.split(" ")

            try {
                leftIsolationBound = parts[0].toDouble()
                rightIsolationBound = parts[1].toDouble()

                if (equation.evaluate(leftIsolationBound).sign * equation.evaluate(rightIsolationBound).sign > 0) {
                    println("No solutions on this isolation interval or function isn't monotonous")
                    continue
                }

                return Pair(leftIsolationBound, rightIsolationBound)
            } catch (e: Throwable) {
                println("Seems like you have written something wrong. Please, reenter:")
                continue
            }
        }
    }
}