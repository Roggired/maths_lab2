package view.method

import model.equation.createEquationFrom
import model.method.HalfDivisionMethod
import view.SceneContext
import java.io.PrintWriter

class HalfDivisionMethodScene: MethodScene() {
    override fun start(sceneContext: SceneContext) {
        sceneContext.router.switch(sceneContext, "Graph")

        val accuracy = sceneContext.data["accuracy"] ?: throw RuntimeException("No accuracy")

        println("Now let's find solutions for this equation with half-division method.")

        val equation = createEquationFrom(sceneContext.equation)

        while (true) {
            val isolationBorders = readIsolationBorders(sceneContext, equation) ?: break

            val method = HalfDivisionMethod(equation, isolationBorders.first, isolationBorders.second, accuracy.toDouble())
            val printWriter = PrintWriter(System.out)

            printWriter.println()
            printWriter.println("Half-Division method table")
            MethodPresenter.presentTable(method.getTable(), printWriter)

            printWriter.println()
            printWriter.println("Half-Division method solution on: ${isolationBorders.first} ${isolationBorders.second}")

            MethodPresenter.presentSolutions(method.getSolutions(), printWriter)
        }
    }
}