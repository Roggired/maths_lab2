package view

class SwitchMethodScene: Scene {
    override fun start(sceneContext: SceneContext) {
        println("Now choose a method (1, 4, 5):")
        println()

        var route = ""
        while (true) {
            val userInput = readNotNullString()

            try {
                when (userInput.toInt()) {
                    1 -> route = "HalfDivisionMethod"
                    4 -> route = "SecantMethod"
                    5 -> route = "SimpleIterationsMethod"
                }

                if (route.isEmpty()) {
                    println("No such method. Please, write:")
                    continue
                }

                break
            } catch (e: Throwable) {
                println("Seems like you have written something wrong. Please, write:")
                continue
            }
        }

        sceneContext.router.switch(sceneContext, route)
    }
}