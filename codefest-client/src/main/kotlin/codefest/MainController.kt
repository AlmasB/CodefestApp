package codefest

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.layout.BorderPane

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class MainController {

    @FXML
    private lateinit var root: BorderPane

    private val views = hashMapOf<String, Parent>()

    fun initialize() {
        onHome()
    }

    fun onHome() {
        setView("home.fxml")
    }

    fun onLeaderboard() {
        setView("top.fxml")
    }

    fun onChallenges() {
        setView("submit.fxml")
    }

    fun onExit() {
        Platform.exit()
    }

    private fun setView(viewName: String) {
        if (viewName !in views) {
            views[viewName] = FXMLLoader.load(javaClass.getResource(viewName))
        }

        root.center = views[viewName]
    }
}