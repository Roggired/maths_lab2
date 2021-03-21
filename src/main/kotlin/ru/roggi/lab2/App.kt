package ru.roggi.lab2

import ru.roggi.comp.math.EasyCompMath
import ru.roggi.comp.math.utils.RouterBuilder
import ru.roggi.console.application.view.scene.Router
import ru.roggi.lab2.view.HalfDivisionMethodScene
import tornadofx.launch

class RouterBuilderImpl: RouterBuilder {
    override fun build(): Router =
        Router()
            .apply {
                register("main", HalfDivisionMethodScene())
            }
}

fun main() {
    launch<EasyCompMath>("main", "ru.roggi.lab2")
}