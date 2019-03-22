package codefest.client

import javafx.fxml.FXML
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
                Views.showLogin()

                println("Logged out")
            }
        }
    }
}