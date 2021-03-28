package ru.roggi.lab2

import ru.roggi.comp.math.EasyCompMath
import ru.roggi.comp.math.utils.RouterBuilder
import ru.roggi.comp.math.view.InputTwoScene
import ru.roggi.console.application.view.scene.ChooseScene
import ru.roggi.console.application.view.scene.Router
import ru.roggi.lab2.model.HalfDivisionMethod
import ru.roggi.lab2.model.SecantMethod
import ru.roggi.lab2.model.SimpleIterationsMethod
import ru.roggi.lab2.view.MethodScene
import tornadofx.launch


const val INPUT_METHOD_BOUNDS_ROUTE = "methodBounds"


class RouterBuilderImpl: RouterBuilder {
    override fun build(): Router =
        Router()
            .apply {
                sceneContext.addExitListener { println("Goodbye!") }
                register("main", ChooseScene(
                    "Choose method (1, 4, 5):",
                    "",
                    listOf("1", "4", "5")
                ) {
                    when (it) {
                        "1" -> "halfDivision"
                        "4" -> "secant"
                        "5" -> "simpleIterations"
                        else -> "main"
                    }
                })
                register(INPUT_METHOD_BOUNDS_ROUTE, InputTwoScene(
                    "Enter bounds for method between -50.0 and 50.0:",
                    String::toDouble,
                    String::toDouble,
                    { left, right ->
                        left in -50.0..50.0
                        && right in -50.0..50.0
                        && left < right
                    }
                ))
                register("halfDivision", MethodScene(
                    "Half-division"
                ) { HalfDivisionMethod(it.equation, it.leftBound, it.rightBound, it.accuracy) })
                register("secant", MethodScene(
                    "Secant"
                ) { SecantMethod(it.equation, it.leftBound, it.rightBound, it.accuracy) })
                register("simpleIterations", MethodScene(
                    "Simple iterations"
                ) { SimpleIterationsMethod(it.equation, it.leftBound, it.rightBound, it.accuracy) })
            }
}

fun main() {
    launch<EasyCompMath>("main", "ru.roggi.lab2")
}