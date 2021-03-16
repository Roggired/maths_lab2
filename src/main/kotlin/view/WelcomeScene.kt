package view

class WelcomeScene : Scene {
    override fun start(sceneContext: SceneContext) {
        println("Welcome to this console-gui monster!")
        println()
        println("I can work with trigonometric, polynomial and logarithmic functions and also it compositions.")
        println()
        println("LIMITATIONS:")
        println("1. Equation doesn't need to contain \"=\".")
        println("2. Equation can contain \"(\" and \")\" only to determine a composition of allowed functions." + System.lineSeparator()
                + "And for all compositions you have to surround their arguments with \"(\" and \")\".")
        println("3. To specify a negative power for a polynomial function don't use \"(\" and \")\".")
        println("4. To specify a power for a polynomial function use \"^\".")
        println("5. You can apply a function only to one another functions. Sums, differences and staff are not allowed yet. ")
        println("6. To specify a logarithmic function write \"log^\" and add a base.")
        println("7. Doubles can be specified only with \".\".")
        println("8. DO NOT use any tabs or spaces.")
        println("9. Free member of an equation has to be the last term.")
        println()
        println("EXAMPLES OF CORRECT EQUATIONS:")
        println("x+x^2")
        println("1.78x+sin(x)")
        println("2.8x+log^2(sin(x))")
        println()
        println("If you want to use default equation, write \"DEFAULT\"")
        println("If you want to use an equation from a file, write \"FILE FILE NAME HERE\"")
        println("If you want to type an equation, write \"CONSOLE EQUATION HERE\"")
        println("If you want to exit, type EXIT")
        println()

        sceneContext.router.switch(sceneContext, "SwitchMode")
    }
}