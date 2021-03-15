package view

class DefaultEquationScene : Scene {
    override fun start(sceneContext: SceneContext) {
        sceneContext.equation = "-2.7x^3-1.48x^2+19.23x+6.35"
        println("Default equation: -2.7x^3-1.48x^2+19.23x+6.35")
        println("Method: Half-Division")
        println()
        sceneContext.router.switch(sceneContext, "HalfDivisionMethod")
    }
}