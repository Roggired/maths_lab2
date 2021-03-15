package view.graph

import javafx.beans.property.Property
import javafx.beans.property.SimpleListProperty
import javafx.collections.ObservableList
import javafx.scene.Cursor
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import model.equation.Equation
import model.equation.createEquationFrom
import tornadofx.*

class GraphView : View("WoW! It is a graph") {
    private val graphService: GraphService by inject()
    private val currentGraph: Property<ObservableList<XYChart.Data<Number, Number>>> = SimpleListProperty()

    init {
        currentGraph.value =
            graphService.getPlotMeta(
                getEquation(),
                getLeftBorder(),
                getRightBorder(),
                getAccuracy()
            ) as? ObservableList<XYChart.Data<Number, Number>>?
    }

    override val root = vbox {
        val equation = getEquation()

        var leftBorder = getLeftBorder()
        var rightBorder = getRightBorder()
        var lowerBorder = equation.evaluate(leftBorder)
        var upperBorder = equation.evaluate(rightBorder)

        if (lowerBorder > upperBorder) {
            val temp = lowerBorder
            lowerBorder = upperBorder
            upperBorder = temp
        }

        leftBorder -= 5
        if (leftBorder >= 0) {
            leftBorder = -10.0
        }
        rightBorder +=5
        if (rightBorder <= 0) {
            rightBorder = 10.0
        }
        lowerBorder -= 100
        upperBorder += 100

        if (lowerBorder >= 0) {
            lowerBorder = -100.0
        }

        if (upperBorder <= 0) {
            upperBorder = 100.0
        }

        linechart(
            "Graph",
            NumberAxis(leftBorder, rightBorder, 1.0),
            NumberAxis(lowerBorder, upperBorder, 1.0)) {
            isLegendVisible = false
            cursor = Cursor.CROSSHAIR

            prefWidth = RootStyles.PREF_WIDTH.toDouble()
            prefHeight = RootStyles.PREF_HEIGHT.toDouble()

            series("Graph", elements = currentGraph.value)
        }
    }

    private fun getEquation(): Equation {
        return createEquationFrom(primaryStage.properties["equation"].toString())
    }

    private fun getLeftBorder(): Double {
        return (primaryStage.properties["leftBorder"] as String).toDouble()
    }

    private fun getRightBorder(): Double {
        return (primaryStage.properties["rightBorder"] as String).toDouble()
    }

    private fun getAccuracy(): Double {
        return (primaryStage.properties["accuracy"] as String).toDouble()
    }
}
