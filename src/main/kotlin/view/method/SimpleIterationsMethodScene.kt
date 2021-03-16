package view.method

import model.equation.Equation
import model.equation.createEquationFrom
import model.method.SimpleIterationsMethod
import view.SceneContext
import java.io.PrintWriter
import kotlin.math.abs

class SimpleIterationsMethodScene: MethodScene() {
    // equation: x^3-x+4
    // phiFunction: 1.091x-0.091x^3-0.364
    // first derivative of phiFunction: -0.273x^2+1.091
    override fun start(sceneContext: SceneContext) {
        sceneContext.router.switch(sceneContext, "Graph")

        val accuracy = sceneContext.data["accuracy"] ?: throw RuntimeException("No accuracy")

        println("Now let's find solutions for this equation with simple iteration method.")

        val equation = createEquationFrom(sceneContext.equation)
        while (true) {
            val isolationBorders = readIsolationBorders(sceneContext, equation) ?: break
            var phiEquation: Equation

            println("Please, write the phi function:")
            while (true) {
                val userInput = readNotNullString()

                try {
                    phiEquation = createEquationFrom(userInput)
                    break
                } catch (e: Throwable) {
                    println("Seems like you have written something wrong. Please, write:")
                    continue
                }
            }

            println()

            var q: Double
            println("Please, write the Lipschitz coefficient:")
            while (true) {
                val userInput = readNotNullString()

                try {
                    q = userInput.toDouble()
                    break
                } catch (e: Throwable) {
                    println("Seems like you have written something wrong. Please, write:")
                    continue
                }
            }

            println()

            println("Unfortunately, I can't find derivatives :(")
            println("Please, write the first derivative for the phiEquation or " +
                    "if I can't evaluate it (see LIMITATIONS for equations) then, " +
                    "please, write \"VALUE FIRST DERIVATIVE IN LEFT BOUND FIRST DERIVATIVE IN RIGHT BOUND\"")

            var firstDerivativeValueInRightBound: Double
            var firstDerivativeValueInLeftBound: Double
            var firstDerivativePhiEquation: Equation
            while (true) {
                val userInput = readNotNullString()

                if (userInput.contains("VALUE")) {
                    try {
                        firstDerivativeValueInLeftBound = userInput.split(" ")[1].toDouble()
                        firstDerivativeValueInRightBound = userInput.split(" ")[2].toDouble()
                    } catch (e: Throwable) {
                        println("Seems like you have written something wrong. Please, write:")
                        continue
                    }
                } else {
                    try {
                        firstDerivativePhiEquation = createEquationFrom(userInput)
                        firstDerivativeValueInRightBound = firstDerivativePhiEquation.evaluate(isolationBorders.second)
                        firstDerivativeValueInLeftBound = firstDerivativePhiEquation.evaluate(isolationBorders.first)
                    } catch (e: Throwable) {
                        println("Seems like you have written something wrong. Please, write:")
                        continue
                    }
                }

                break
            }

            if (abs(firstDerivativeValueInLeftBound) > q || abs(firstDerivativeValueInRightBound) > q) {
                println("I can't apply the secant method for this isolation borders.")
                continue
            }

            val x0 = if (abs(firstDerivativeValueInLeftBound) <= q) {
                isolationBorders.first
            } else {
                isolationBorders.second
            }

            val method = SimpleIterationsMethod(q, phiEquation, x0, equation, isolationBorders.first, isolationBorders.second, accuracy.toDouble())
            val printWriter = PrintWriter(System.out)

            printWriter.println()
            printWriter.println("Simple iterations method table")
            MethodPresenter.presentTable(method.getTable(), printWriter)

            printWriter.println()
            printWriter.println("Simple iterations method solution on: ${isolationBorders.first} ${isolationBorders.second}")

            MethodPresenter.presentSolutions(method.getSolutions(), printWriter)
        }
    }
}