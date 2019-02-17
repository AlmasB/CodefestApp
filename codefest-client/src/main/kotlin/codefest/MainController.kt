package codefest

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
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
}