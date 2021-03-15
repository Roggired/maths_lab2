package view

import tornadofx.launch
import view.graph.GraphLauncher

class DefaultEquationScene : Scene {
    override fun start(sceneContext: SceneContext) {
        launch<GraphLauncher>("-2.7x^3-1.48x^2+19.23x+6.35", "-5", "5", "0.01")
    }
}