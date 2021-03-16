package view

class SceneContext {
    val router = Router()
    var equation: String = ""
    var data: MutableMap<String, String> = mutableMapOf()
    var graphThread: Thread? = null
}