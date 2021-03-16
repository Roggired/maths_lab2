package view

import model.equation.Equation
import model.equation.createEquationFrom
import model.method.SecantMethod
import view.method.MethodPresenter
import java.io.PrintWriter
import kotlin.math.sign

class SecantMethodScene: MethodScene() {
    override fun start(sceneContext: SceneContext) {
        sceneContext.router.switch(sceneContext, "Graph")

        val accuracy = sceneContext.data["accuracy"] ?: throw RuntimeException("No accuracy")

        println("Now let's find solutions for this equation with secant method.")

        val equation = createEquationFrom(sceneContext.equation)

        while (true) {
            val isolationBorders = readIsolationBorders(sceneContext, equation) ?: break

            println("Unfortunately, I can't find derivatives :(")
            println("Please, write the second derivative for the equation or " +
                    "if I can't evaluate it (see LIMITATIONS for equations) then, " +
                    "please, write \"VALUE SECOND DERIVATIVE IN LEFT BOUND SECOND DERIVATIVE IN RIGHT BOUND\"")

            var secondDerivativeValueInRightBound: Double
            var secondDerivativeValueInLeftBound: Double
            var secondDerivative: Equation
            while (true) {
                val userInput = readNotNullString()

                if (userInput.contains("VALUE")) {
                    try {
                        secondDerivativeValueInLeftBound = userInput.split(" ")[1].toDouble()
                        secondDerivativeValueInRightBound = userInput.split(" ")[2].toDouble()
                    } catch (e: Throwable) {
                        println("Seems like you have written something wrong. Please, write:")
                        continue
                    }
                } else {
                    try {
                        secondDerivative = createEquationFrom(userInput)
                        secondDerivativeValueInRightBound = secondDerivative.evaluate(isolationBorders.second)
                        secondDerivativeValueInLeftBound = secondDerivative.evaluate(isolationBorders.first)
                    } catch (e: Throwable) {
                        println("Seems like you have written something wrong. Please, write:")
                        continue
                    }
                }

                break
            }

            val temp1 = equation.evaluate(isolationBorders.first)
            val temp2 = equation.evaluate(isolationBorders.second)

            if (equation.evaluate(isolationBorders.second).sign * secondDerivativeValueInRightBound.sign < 0 &&
                    equation.evaluate(isolationBorders.first).sign * secondDerivativeValueInLeftBound.sign < 0) {
                println("I can't apply the secant method for this isolation borders.")
                continue
            }

            val method = SecantMethod(equation, secondDerivativeValueInLeftBound, secondDerivativeValueInRightBound, isolationBorders.first, isolationBorders.second, accuracy.toDouble())
            val printWriter = PrintWriter(System.out)

            printWriter.println()
            printWriter.println("Secant method table")
            MethodPresenter.presentTable(method.getTable(), printWriter)

            printWriter.println()
            printWriter.println("Secant method solution on: ${isolationBorders.first} ${isolationBorders.second}")

            MethodPresenter.presentSolutions(method.getSolutions(), printWriter)
        }
    }
}