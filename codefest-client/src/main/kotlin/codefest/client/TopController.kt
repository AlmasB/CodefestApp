package codefest.client

import codefest.common.data.Leaderboard
import codefest.common.data.Student
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.ListView

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class TopController {

    @FXML
    private lateinit var listView: ListView<Student>

    fun initialize() {




    }

    private fun updateListView(leaderboard: Leaderboard) {
        Platform.runLater {
            listView.items.setAll(leaderboard.students)
        }
    }
}