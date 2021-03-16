package view

interface Scene {
    fun start(sceneContext: SceneContext)

    fun readNotNullString(): String {
        while (true) {
            val userInput = readLine()

            if (userInput != null) {
                return userInput
            }
            println("Seems like you have written nothing. Please, write:")
        }
    }
}