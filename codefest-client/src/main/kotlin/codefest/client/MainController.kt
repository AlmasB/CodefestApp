package codefest.client

import javafx.fxml.FXML
import javafx.scene.layout.BorderPane

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class MainController {

    @FXML
    private lateinit var root: BorderPane

    private val viewPool = ViewPool()

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

    private fun setView(viewName: String) {
        root.center = viewPool.obtain(viewName)
    }
}