package codefest.client

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Button

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class HomeController {

    @FXML
    private lateinit var btnLogout: Button

    fun onLogOut() {
        Server.requestLogout {
            onSuccess = {
                val scene = btnLogout.scene
                scene.root = FXMLLoader.load(javaClass.getResource("login.fxml"))

                println("Logged out")
            }
        }
    }
}