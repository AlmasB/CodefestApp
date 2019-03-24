package codefest.client

import javafx.fxml.FXML
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.BorderPane

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class MainController {

    @FXML
    private lateinit var tabLeaderboard: Tab
    @FXML
    private lateinit var leaderboardController: LeaderboardController

    fun initialize() {
        tabLeaderboard.selectedProperty().addListener { _, _, isSelected ->
            if (isSelected) {
                leaderboardController.requestLeaderboard()
            }
        }
    }
}