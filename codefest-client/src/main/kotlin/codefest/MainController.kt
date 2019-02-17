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

    fun initialize() {
        root.center = FXMLLoader.load(javaClass.getResource("submit.fxml"))
    }

    fun onHome() {

    }

    fun onLeaderboard() {
        setView(FXMLLoader.load(javaClass.getResource("top.fxml")))
    }

    fun onChallenges() {

    }

    fun onExit() {
        Platform.exit()
    }

    private fun setView(parent: Parent) {
        root.center = parent
    }
}