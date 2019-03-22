package codefest.client

import javafx.scene.Scene

object Views {

    lateinit var scene : Scene

    private val viewPool = ViewPool()

    fun showLogin() {
        setView("login.fxml")
    }

    fun showMain() {
        setView("main.fxml")
    }

    private fun setView(viewName: String) {
        scene.root = viewPool.obtain(viewName)
    }
}