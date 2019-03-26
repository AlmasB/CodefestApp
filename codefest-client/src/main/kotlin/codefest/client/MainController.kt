package codefest.client

import javafx.fxml.FXML
import javafx.scene.control.Tab

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