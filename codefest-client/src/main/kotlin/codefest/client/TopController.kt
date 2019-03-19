package codefest.client

import codefest.common.data.Leaderboard
import codefest.common.data.Student
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.ListView
import javafx.scene.text.Text

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class TopController {

    @FXML
    private lateinit var listView: ListView<Student>

    @FXML
    private lateinit var tv: Text

    fun initialize() {
        Server.requestLeaderboard {
            onSuccess = {
                updateListView(it)
            }
        }
    }

    private fun updateListView(leaderboard: Leaderboard) {
        Platform.runLater {
            tv.text = "Name:\tScore"
            listView.items.setAll(leaderboard.students)
        }
    }
}