package view

class SwitchModeScene : Scene {
    override fun start(sceneContext: SceneContext) {
        println("I am waiting for your input:")

        var targetRoute: String?

        while (true) {
            val userInput = readLine()

            if (userInput == null) {
                println()
                println("Please, enter something:")
                continue;
            }

            val option = userInput.trim();

            if (option == "EXIT") {
                println("Good bye!")
                return
            }

            if (option == "DEFAULT") {
                targetRoute = "DefaultEquation"
                break
            }

            if (option.startsWith("FILE")) {
                targetRoute = "FileEquation"
                try {
                    sceneContext.data["fileName"] = option.split(" ")[1]
                    break
                } catch (e: Throwable) {
                    println("Seems like you have forgotten to specify the file name. Please, write:")
                    continue
                }
            }

            if (option.startsWith("CONSOLE")) {
                targetRoute = "ConsoleEquation"
                try {
                    sceneContext.equation = option.split(" ")[1]
                    break
                } catch (e: Throwable) {
                    println("Seems like you have forgotten to specify the equation. Please, write:")
                    continue
                }
            }

            println()
            println("Oops! You have written something that, unfortunately, I can't understand :( Please, write again:")
        }

        sceneContext.router.switch(sceneContext, targetRoute ?: "Welcome")
    }
}