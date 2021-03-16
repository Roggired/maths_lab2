package view

class Router {
    private var scenes: Map<String, Scene> = mapOf(Pair<String, Scene>("Welcome", WelcomeScene()),
                                           Pair<String, Scene>("SwitchMode", SwitchModeScene()),
                                           Pair<String, Scene>("DefaultEquation", DefaultEquationScene()),
                                           Pair<String, Scene>("FileEquation", FileEquationScene()),
                                           Pair<String, Scene>("ConsoleEquation", ConsoleEquationScene()),
                                           Pair<String, Scene>("HalfDivisionMethod", HalfDivisionMethodScene()),
                                           Pair<String, Scene>("SwitchMethod", SwitchMethodScene()),
                                           Pair<String, Scene>("Graph", GraphScene()),
                                           Pair<String, Scene>("SecantMethod", SecantMethodScene())
    )

    fun switch(sceneContext: SceneContext, to: String) {
        scenes[to]?.start(sceneContext)
    }
}