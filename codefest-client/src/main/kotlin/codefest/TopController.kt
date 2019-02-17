package codefest

import codefest.data.Leaderboard
import codefest.data.Student
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.ListView
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

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