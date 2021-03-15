package view.graph

import javafx.stage.Stage
import tornadofx.App
import tornadofx.importStylesheet

class GraphLauncher : App(GraphView::class, RootStyles::class) {
    override fun start(stage: Stage) {
        importStylesheet("/style.css")

        stage.properties["equation"] = parameters.raw[0]
        stage.properties["leftBorder"] = parameters.raw[1]
        stage.properties["rightBorder"] = parameters.raw[2]
        stage.properties["accuracy"] = parameters.raw[3]
        super.start(stage)
    }
}