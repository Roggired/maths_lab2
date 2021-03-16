package view

import java.io.FileInputStream
import java.util.*

class FileEquationScene : Scene {
    override fun start(sceneContext: SceneContext) {
        val fileName = sceneContext.data["fileName"] ?: throw NullPointerException()

        val scanner = Scanner(FileInputStream(fileName))
        val equation = scanner.nextLine() ?: throw RuntimeException("Empty file")
        scanner.close()

        sceneContext.equation = equation

        sceneContext.router.switch(sceneContext, "SwitchMethod")
    }
}