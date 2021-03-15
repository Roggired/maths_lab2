import view.SceneContext

fun main() {
    val sceneContext = SceneContext()
    sceneContext.router.switch(sceneContext, "Welcome")
}