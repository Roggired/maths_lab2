package view.method

import java.io.PrintWriter

class MethodPresenter {
    companion object {
        fun presentTable(table: ArrayList<Array<String>>, io: PrintWriter) {
            table.forEach {
                it.forEach {
                    try {
                        val double = it.toDouble()
                        io.print("%15.5f".format(double))
                    } catch (e: Throwable) {
                        io.print("%15s".format(it))
                    }
                }
                io.println()
            }
            io.flush()
        }

        fun presentSolutions(solutions: ArrayList<Double>, io: PrintWriter) {
            solutions.forEach {
                io.print("%10.5f".format(it))
            }
            io.println()
            io.flush()
        }
    }
}