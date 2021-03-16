package view

class ConsoleEquationScene : Scene {
    override fun start(sceneContext: SceneContext) {
        sceneContext.router.switch(sceneContext, "SwitchMethod")
    }
}