package view

import javafx.application.Platform
import tornadofx.launch
import view.graph.GraphLauncher
import java.lang.RuntimeException

class GraphScene: Scene {
    override fun start(sceneContext: SceneContext) {
        println("Enter leftBound rightBound and accuracy:")

        var leftBound: Double
        var rightBound: Double
        var accuracy: Double
        while (true) {
            val userInput = readNotNullString()

            val parts = userInput.split(" ")

            try {
                leftBound = parts[0].toDouble()
                rightBound = parts[1].toDouble()
                accuracy = parts[2].toDouble()
                break
            } catch (e: Throwable) {
                println("Seems like you have written something wrong. Please, reenter:")
                continue
            }
        }

        sceneContext.data["accuracy"] = accuracy.toString()

        sceneContext.graphThread = Thread {
            try {
                launch<GraphLauncher>(
                    sceneContext.equation,
                    leftBound.toString(),
                    rightBound.toString(),
                    accuracy.toString()
                )
            } catch (e: RuntimeException) {
                if (e.cause is InterruptedException) {
                    Platform.exit()
                }
            }
        }
        sceneContext.graphThread?.start()

        println()
        println("I have drawn the graph for you! You can see it in the separate window.")
    }
}