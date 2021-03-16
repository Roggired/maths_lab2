package view

class DefaultEquationScene : Scene {
    override fun start(sceneContext: SceneContext) {
        sceneContext.equation = "-2.7x^3-1.48x^2+19.23x+6.35"
        println("Default equation: -2.7x^3-1.48x^2+19.23x+6.35")
        // first derivative -8.1x^2-2.96x+19.23
        // second derivative -16.2x-2.96
        println()
        sceneContext.router.switch(sceneContext, "SwitchMethod")
    }
}