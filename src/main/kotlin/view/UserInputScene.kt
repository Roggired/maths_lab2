package view

class UserInputScene : Scene {
    override fun start(sceneContext: SceneContext) {
        println("I am waiting for your input:")

        val targetRoute: String?

        while (true) {
            val userInput = readLine()

            if (userInput == null) {
                println()
                println("Please, enter something:")
                continue;
            }

            val option = userInput.trim();

            if (option == "DEFAULT") {
                targetRoute = "DefaultEquation"
                break
            }

            if (option.startsWith("FILE")) {
                targetRoute = "FileEquation"
                break
            }

            if (option.contains('=')) {
                targetRoute = "ConsoleEquation"
                break
            }

            println()
            println("Oops! You have written something that, unfortunately, I can't understand :( Please, write again:")
        }

        sceneContext.router.switch(sceneContext, targetRoute ?: "UserInput")
    }
}